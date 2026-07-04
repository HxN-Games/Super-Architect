package com.hxngames.superarchitect.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class DiskRackBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty BAY1 = IntegerProperty.create("bay1", 0, 3);
    public static final IntegerProperty BAY2 = IntegerProperty.create("bay2", 0, 3);
    public static final IntegerProperty BAY3 = IntegerProperty.create("bay3", 0, 3);
    public static final IntegerProperty BAY4 = IntegerProperty.create("bay4", 0, 3);

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
                .setValue(BAY4, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
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
        builder.add(FACING, BAY1, BAY2, BAY3, BAY4);
    }
}
