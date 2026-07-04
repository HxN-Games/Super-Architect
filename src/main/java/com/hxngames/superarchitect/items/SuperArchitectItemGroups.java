package com.hxngames.superarchitect.items;

import com.hxngames.superarchitect.SuperArchitect;
import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SuperArchitectItemGroups {
    public static final CreativeModeTab SUPER_ARCHITECT_GROUP = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(SuperArchitect.MOD_ID, "main"),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemgroup.super_architect.main"))
                    .icon(() -> new ItemStack(SuperArchitectBlocks.DISK_RACK))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(SuperArchitectBlocks.DISK_RACK);
                        entries.accept(SuperArchitectBlocks.MONITOR);
                        entries.accept(SuperArchitectBlocks.PIPE);
                        entries.accept(SuperArchitectItems.DISK_128);
                        entries.accept(SuperArchitectItems.DISK_256);
                        entries.accept(SuperArchitectItems.DISK_512);
                        entries.accept(SuperArchitectItems.DISK_1k);
                        entries.accept(SuperArchitectItems.DISK_2k);
                        entries.accept(SuperArchitectItems.DISK_4k);
                        entries.accept(SuperArchitectItems.DISK_8k);
                        entries.accept(SuperArchitectItems.CHIP);
                        entries.accept(SuperArchitectItems.RAW_SILICA);
                        entries.accept(SuperArchitectItems.SILICON);
                    })
                    .build()
    );

    public static void initialize() {
    }
}
