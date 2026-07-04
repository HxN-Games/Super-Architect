package com.hxngames.superarchitect.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PipeBlock extends Block {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    // A small center box for the pipe
    private static final VoxelShape CENTER = Block.box(5, 5, 5, 11, 11, 11);

    public PipeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(NORTH, connectsTo(level, pos.north(), Direction.NORTH))
                .setValue(EAST, connectsTo(level, pos.east(), Direction.EAST))
                .setValue(SOUTH, connectsTo(level, pos.south(), Direction.SOUTH))
                .setValue(WEST, connectsTo(level, pos.west(), Direction.WEST))
                .setValue(UP, connectsTo(level, pos.above(), Direction.UP))
                .setValue(DOWN, connectsTo(level, pos.below(), Direction.DOWN));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        BooleanProperty property = getPropertyForDirection(direction);
        if (property != null) {
            return state.setValue(property, connectsTo(level, neighborPos, direction));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    private boolean connectsTo(BlockGetter level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        // Connects to other network blocks
        return block instanceof PipeBlock || block instanceof DiskRackBlock || block instanceof MonitorBlock;
    }

    private BooleanProperty getPropertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = CENTER;
        if (state.getValue(NORTH)) shape = Shapes.or(shape, Block.box(5, 5, 0, 11, 11, 5));
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, Block.box(5, 5, 11, 11, 11, 16));
        if (state.getValue(EAST)) shape = Shapes.or(shape, Block.box(11, 5, 5, 16, 11, 11));
        if (state.getValue(WEST)) shape = Shapes.or(shape, Block.box(0, 5, 5, 5, 11, 11));
        if (state.getValue(UP)) shape = Shapes.or(shape, Block.box(5, 11, 5, 11, 16, 11));
        if (state.getValue(DOWN)) shape = Shapes.or(shape, Block.box(5, 0, 5, 11, 5, 11));
        return shape;
    }
}
