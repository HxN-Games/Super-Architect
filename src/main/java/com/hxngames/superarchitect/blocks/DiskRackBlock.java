package com.hxngames.superarchitect.blocks;

import com.hxngames.superarchitect.blockentities.DiskRackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiskRackBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty BAY1 = IntegerProperty.create("bay1", 0, 3);
    public static final IntegerProperty BAY2 = IntegerProperty.create("bay2", 0, 3);
    public static final IntegerProperty BAY3 = IntegerProperty.create("bay3", 0, 3);
    public static final IntegerProperty BAY4 = IntegerProperty.create("bay4", 0, 3);

    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");

    public DiskRackBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .instrument(NoteBlockInstrument.BASEDRUM)
                        .requiresCorrectToolForDrops()
                        .strength(3.5F)
                );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(BAY1, 0)
                .setValue(BAY2, 0)
                .setValue(BAY3, 0)
                .setValue(BAY4, 0)
                .setValue(CONNECTED_LEFT, false)
                .setValue(CONNECTED_RIGHT, false)
                .setValue(CONNECTED_UP, false)
                .setValue(CONNECTED_DOWN, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        boolean connectedLeft = context.getLevel().getBlockState(context.getClickedPos().relative(facing.getClockWise())).is(this) 
            && context.getLevel().getBlockState(context.getClickedPos().relative(facing.getClockWise())).getValue(FACING) == facing;
        boolean connectedRight = context.getLevel().getBlockState(context.getClickedPos().relative(facing.getCounterClockWise())).is(this)
            && context.getLevel().getBlockState(context.getClickedPos().relative(facing.getCounterClockWise())).getValue(FACING) == facing;
        boolean connectedUp = context.getLevel().getBlockState(context.getClickedPos().relative(Direction.UP)).is(this)
            && context.getLevel().getBlockState(context.getClickedPos().relative(Direction.UP)).getValue(FACING) == facing;
        boolean connectedDown = context.getLevel().getBlockState(context.getClickedPos().relative(Direction.DOWN)).is(this)
            && context.getLevel().getBlockState(context.getClickedPos().relative(Direction.DOWN)).getValue(FACING) == facing;
        
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(CONNECTED_LEFT, connectedLeft)
                .setValue(CONNECTED_RIGHT, connectedRight)
                .setValue(CONNECTED_UP, connectedUp)
                .setValue(CONNECTED_DOWN, connectedDown);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING);
        if (direction == facing.getClockWise()) {
            return state.setValue(CONNECTED_LEFT, neighborState.is(this) && neighborState.getValue(FACING) == facing);
        }
        if (direction == facing.getCounterClockWise()) {
            return state.setValue(CONNECTED_RIGHT, neighborState.is(this) && neighborState.getValue(FACING) == facing);
        }
        if (direction == Direction.UP) {
            return state.setValue(CONNECTED_UP, neighborState.is(this) && neighborState.getValue(FACING) == facing);
        }
        if (direction == Direction.DOWN) {
            return state.setValue(CONNECTED_DOWN, neighborState.is(this) && neighborState.getValue(FACING) == facing);
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    protected @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BAY1, BAY2, BAY3, BAY4, CONNECTED_LEFT, CONNECTED_RIGHT, CONNECTED_UP, CONNECTED_DOWN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(net.minecraft.core.BlockPos pos, BlockState state) {
        return new DiskRackBlockEntity(pos, state);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DiskRackBlockEntity diskRack) {
                player.openMenu(diskRack);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DiskRackBlockEntity diskRack) {
                Containers.dropContents(level, pos, diskRack.getInventory());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
