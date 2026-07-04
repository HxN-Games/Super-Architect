package com.hxngames.superarchitect.blocks;

import com.hxngames.superarchitect.registry.RegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static com.hxngames.superarchitect.SuperArchitect.MOD_ID;

public class SuperArchitectBlocks {
    public static final Block DISK_RACK = RegistryHelper.registerBlockWithItem(MOD_ID, "disk_rack", new DiskRackBlock(), new Item.Properties());
    public static final Block MONITOR = RegistryHelper.registerBlockWithItem(MOD_ID, "monitor", new MonitorBlock(), new Item.Properties());

    public static void initialize() {}
}
