package com.hxngames.superarchitect;

import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import com.hxngames.superarchitect.items.SuperArchitectItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuperArchitect implements ModInitializer {
    public static String MOD_ID = "super_architect";
    public static Logger LOGGER = LoggerFactory.getLogger("SuperArchitect");

    private static final SuperArchitectItems superArchitectItems = new SuperArchitectItems();
    private static final SuperArchitectBlocks superArchitectBlocks = new SuperArchitectBlocks();

    @Override
    public void onInitialize() {
        superArchitectItems.initialize();
        superArchitectBlocks.initialize();

        LOGGER.info("SuperArchitect Initialized!");
    }
}
