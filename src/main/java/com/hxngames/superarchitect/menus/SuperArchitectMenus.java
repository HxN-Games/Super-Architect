package com.hxngames.superarchitect.menus;

import com.hxngames.superarchitect.registry.RegistryHelper;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class SuperArchitectMenus {
    public static final MenuType<DiskRackMenu> DISK_RACK_MENU = RegistryHelper.registerMenuType(
            "super_architect",
            "disk_rack",
            new MenuType<>(DiskRackMenu::new, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS)
    );

    public static final MenuType<MonitorMenu> MONITOR_MENU = RegistryHelper.registerMenuType(
            "super_architect",
            "monitor",
            new MenuType<>(MonitorMenu::new, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS)
    );

    public static void initialize() {}
}
