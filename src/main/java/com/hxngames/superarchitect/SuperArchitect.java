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
        com.hxngames.superarchitect.items.SuperArchitectItemGroups.initialize();
        SuperArchitectBlockEntities.initialize();
        SuperArchitectMenus.initialize();

        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playC2S().register(
                com.hxngames.superarchitect.network.MonitorSearchPacket.TYPE,
                com.hxngames.superarchitect.network.MonitorSearchPacket.CODEC
        );
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(
                com.hxngames.superarchitect.network.MonitorSearchPacket.TYPE,
                (payload, context) -> {
                    if (context.player().containerMenu instanceof com.hxngames.superarchitect.menus.MonitorMenu menu) {
                        menu.setSearchQuery(payload.query());
                    }
                }
        );

        LOGGER.info("SuperArchitect Initialized!");
    }
}
