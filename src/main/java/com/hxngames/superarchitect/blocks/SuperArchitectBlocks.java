package com.hxngames.superarchitect.blocks;

import com.hxngames.superarchitect.registry.RegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static com.hxngames.superarchitect.SuperArchitect.MOD_ID;

public class SuperArchitectBlocks {
    public static final Block DISK_RACK = RegistryHelper.registerBlockWithItem(MOD_ID, "disk_rack", new DiskRackBlock(), new Item.Properties());
    public static final Block MONITOR = RegistryHelper.registerBlockWithItem(MOD_ID, "monitor", new MonitorBlock(), new Item.Properties());
    public static final Block PIPE = RegistryHelper.registerBlockWithItem(MOD_ID, "pipe", new PipeBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().mapColor(net.minecraft.world.level.material.MapColor.METAL).strength(3.0F)), new Item.Properties());
    public static final Block BLANK_MONITOR = RegistryHelper.registerBlockWithItem(MOD_ID, "blank_monitor", new BlankMonitorBlock(), new Item.Properties());

    public static void initialize() {}
}
