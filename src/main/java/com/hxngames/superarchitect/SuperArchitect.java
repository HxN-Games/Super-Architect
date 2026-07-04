package com.hxngames.superarchitect;

import com.hxngames.superarchitect.blockentities.SuperArchitectBlockEntities;
import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import com.hxngames.superarchitect.items.SuperArchitectItems;
import com.hxngames.superarchitect.menus.SuperArchitectMenus;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuperArchitect implements ModInitializer {
    public static String MOD_ID = "super_architect";
    public static Logger LOGGER = LoggerFactory.getLogger("SuperArchitect");

    @Override
    public void onInitialize() {
        SuperArchitectItems.initialize();
        SuperArchitectBlocks.initialize();
        SuperArchitectBlockEntities.initialize();
        SuperArchitectMenus.initialize();

        LOGGER.info("SuperArchitect Initialized!");
    }
}
