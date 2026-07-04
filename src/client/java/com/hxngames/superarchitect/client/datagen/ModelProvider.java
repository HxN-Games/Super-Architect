package com.hxngames.superarchitect.client.datagen;

import com.hxngames.superarchitect.blocks.DiskRackBlock;
import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import com.hxngames.superarchitect.items.SuperArchitectItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        var multipart = MultiPartGenerator.multiPart(SuperArchitectBlocks.DISK_RACK);

        IntegerProperty[] bays = {
            DiskRackBlock.BAY1,
            DiskRackBlock.BAY2,
            DiskRackBlock.BAY3,
            DiskRackBlock.BAY4
        };

        for (Direction dir : HorizontalDirectionalBlock.FACING.getPossibleValues()) {
            VariantProperties.Rotation rot = switch (dir) {
                case EAST -> VariantProperties.Rotation.R180;
                case SOUTH -> VariantProperties.Rotation.R270;
                case WEST -> VariantProperties.Rotation.R0;
                default -> VariantProperties.Rotation.R90;
            };

            // Base part
            multipart.with(
                net.minecraft.data.models.blockstates.Condition.condition().term(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, dir),
                net.minecraft.data.models.blockstates.Variant.variant()
                    .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("super_architect", "block/disk_rack_parts/disk_rack_base"))
                    .with(net.minecraft.data.models.blockstates.VariantProperties.Y_ROT, rot)
            );

            // Outline Left
            multipart.with(
                net.minecraft.data.models.blockstates.Condition.condition()
                    .term(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, dir)
                    .term(com.hxngames.superarchitect.blocks.DiskRackBlock.CONNECTED_LEFT, false),
                net.minecraft.data.models.blockstates.Variant.variant()
                    .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("super_architect", "block/disk_rack_parts/disk_rack_outline_left"))
                    .with(net.minecraft.data.models.blockstates.VariantProperties.Y_ROT, rot)
            );

            // Outline Right
            multipart.with(
                net.minecraft.data.models.blockstates.Condition.condition()
                    .term(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, dir)
                    .term(com.hxngames.superarchitect.blocks.DiskRackBlock.CONNECTED_RIGHT, false),
                net.minecraft.data.models.blockstates.Variant.variant()
                    .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("super_architect", "block/disk_rack_parts/disk_rack_outline_right"))
                    .with(net.minecraft.data.models.blockstates.VariantProperties.Y_ROT, rot)
            );

            // Outline Top
            multipart.with(
                net.minecraft.data.models.blockstates.Condition.condition()
                    .term(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, dir)
                    .term(com.hxngames.superarchitect.blocks.DiskRackBlock.CONNECTED_UP, false),
                net.minecraft.data.models.blockstates.Variant.variant()
                    .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("super_architect", "block/disk_rack_parts/disk_rack_outline_top"))
                    .with(net.minecraft.data.models.blockstates.VariantProperties.Y_ROT, rot)
            );

            // Outline Bottom
            multipart.with(
                net.minecraft.data.models.blockstates.Condition.condition()
                    .term(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, dir)
                    .term(com.hxngames.superarchitect.blocks.DiskRackBlock.CONNECTED_DOWN, false),
                net.minecraft.data.models.blockstates.Variant.variant()
                    .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("super_architect", "block/disk_rack_parts/disk_rack_outline_bottom"))
                    .with(net.minecraft.data.models.blockstates.VariantProperties.Y_ROT, rot)
            );

            // Bay parts
            for (int i = 0; i < 4; i++) {
                for (int stage = 0; stage < 4; stage++) {
                    multipart.with(
                        Condition.condition()
                            .term(HorizontalDirectionalBlock.FACING, dir)
                            .term(bays[i], stage),
                        Variant.variant()
                            .with(VariantProperties.MODEL, ResourceLocation.fromNamespaceAndPath("super_architect", "block/disk_rack_parts/disk_rack_bay" + (i + 1) + "_stage" + stage))
                            .with(VariantProperties.Y_ROT, rot)
                    );
                }
            }
        }

        blockStateModelGenerator.blockStateOutput.accept(multipart);

        blockStateModelGenerator.delegateItemModel(SuperArchitectBlocks.DISK_RACK, ModelLocationUtils.getModelLocation(SuperArchitectBlocks.DISK_RACK));

        // Monitor Block
        blockStateModelGenerator.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(SuperArchitectBlocks.MONITOR, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(SuperArchitectBlocks.MONITOR)))
                .with(PropertyDispatch.property(HorizontalDirectionalBlock.FACING)
                    .select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                    .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                    .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                )
        );
        blockStateModelGenerator.delegateItemModel(SuperArchitectBlocks.MONITOR, ModelLocationUtils.getModelLocation(SuperArchitectBlocks.MONITOR));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(SuperArchitectItems.DISK_128, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(SuperArchitectItems.DISK_256, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(SuperArchitectItems.DISK_512, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(SuperArchitectItems.DISK_1k, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(SuperArchitectItems.DISK_2k, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(SuperArchitectItems.DISK_4k, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(SuperArchitectItems.DISK_8k, ModelTemplates.FLAT_ITEM);
    }
}
