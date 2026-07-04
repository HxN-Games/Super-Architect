package com.hxngames.superarchitect.client;

import com.hxngames.superarchitect.client.screens.DiskRackScreen;
import com.hxngames.superarchitect.menus.SuperArchitectMenus;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class SuperArchitectClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        net.minecraft.client.gui.screens.MenuScreens.register(com.hxngames.superarchitect.menus.SuperArchitectMenus.DISK_RACK_MENU, com.hxngames.superarchitect.client.screens.DiskRackScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(com.hxngames.superarchitect.menus.SuperArchitectMenus.MONITOR_MENU, com.hxngames.superarchitect.client.screens.MonitorScreen::new);
    }
}
