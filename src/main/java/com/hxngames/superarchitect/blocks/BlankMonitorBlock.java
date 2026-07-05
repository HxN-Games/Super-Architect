package com.hxngames.superarchitect.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlankMonitorBlock extends Block {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public BlankMonitorBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .instrument(NoteBlockInstrument.BASEDRUM)
                        .requiresCorrectToolForDrops()
                        .strength(3.5F)
        );

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();

        return this.defaultBlockState()
                .setValue(FACING, facing);
    }

    @Override
    protected @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    protected static final VoxelShape NORTH_SHAPE = Block.box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
    protected static final VoxelShape WEST_SHAPE = Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D);
    protected static final VoxelShape UP_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    protected static final VoxelShape DOWN_SHAPE = Block.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
