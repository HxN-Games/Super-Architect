package com.hxngames.superarchitect.items;

import com.hxngames.superarchitect.items.components.DiskComponent;
import com.hxngames.superarchitect.registry.RegistryHelper;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;

import static com.hxngames.superarchitect.SuperArchitect.LOGGER;
import static com.hxngames.superarchitect.SuperArchitect.MOD_ID;

public class SuperArchitectItems {
    public static Item DISK_128 =  RegistryHelper.registerItem(MOD_ID, "disk_128", new DiskItem(128));
    public static Item DISK_256 =  RegistryHelper.registerItem(MOD_ID, "disk_256", new DiskItem(256));
    public static Item DISK_512 =  RegistryHelper.registerItem(MOD_ID, "disk_512", new DiskItem(512));
    public static Item DISK_1k =  RegistryHelper.registerItem(MOD_ID, "disk_1024", new DiskItem(1024));
    public static Item DISK_2k =  RegistryHelper.registerItem(MOD_ID, "disk_2048", new DiskItem(2048));
    public static Item DISK_4k =  RegistryHelper.registerItem(MOD_ID, "disk_4096", new DiskItem(4096));
    public static Item DISK_8k =  RegistryHelper.registerItem(MOD_ID, "disk_8192", new DiskItem(8192));
    public static Item CHIP = RegistryHelper.registerItem(MOD_ID, "chip", new Item(new Item.Properties()));
    public static Item RAW_SILICA = RegistryHelper.registerItem(MOD_ID, "raw_silica", new Item(new Item.Properties()));
    public static Item SILICON = RegistryHelper.registerItem(MOD_ID, "silicon", new Item(new Item.Properties()));

    public static final DataComponentType<DiskComponent> DISK_COMPONENT = RegistryHelper.registerDataComponentType(
            MOD_ID, "disk_component",
            DataComponentType.<DiskComponent>builder()
                    .persistent(DiskComponent.CODEC)
                    .networkSynchronized(DiskComponent.STREAM_CODEC)
                    .build()
    );

    public static void initialize() {}
}
