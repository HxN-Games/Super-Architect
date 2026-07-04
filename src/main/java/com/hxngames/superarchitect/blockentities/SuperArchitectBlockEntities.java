package com.hxngames.superarchitect.blockentities;

import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import com.hxngames.superarchitect.registry.RegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SuperArchitectBlockEntities {
    public static final BlockEntityType<DiskRackBlockEntity> DISK_RACK = RegistryHelper.registerBlockEntity(
            "super_architect",
            "disk_rack",
            BlockEntityType.Builder.of(DiskRackBlockEntity::new, SuperArchitectBlocks.DISK_RACK).build()
    );

    public static final BlockEntityType<MonitorBlockEntity> MONITOR = RegistryHelper.registerBlockEntity(
            "super_architect",
            "monitor",
            BlockEntityType.Builder.of(MonitorBlockEntity::new, SuperArchitectBlocks.MONITOR).build()
    );

    public static void initialize() {}
}
