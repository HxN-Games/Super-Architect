package com.hxngames.superarchitect.client;

import com.hxngames.superarchitect.client.screens.DiskRackScreen;
import com.hxngames.superarchitect.menus.SuperArchitectMenus;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class SuperArchitectClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(SuperArchitectMenus.DISK_RACK_MENU, DiskRackScreen::new);
    }
}
