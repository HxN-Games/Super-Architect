package com.hxngames.superarchitect;

import com.hxngames.superarchitect.blockentities.SuperArchitectBlockEntities;
import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import com.hxngames.superarchitect.items.SuperArchitectItemGroups;
import com.hxngames.superarchitect.items.SuperArchitectItems;
import com.hxngames.superarchitect.menus.MonitorMenu;
import com.hxngames.superarchitect.menus.SuperArchitectMenus;
import com.hxngames.superarchitect.network.MonitorSearchPacket;
import com.hxngames.superarchitect.recipes.SuperArchitectRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuperArchitect implements ModInitializer {
    public static String MOD_ID = "super_architect";
    public static Logger LOGGER = LoggerFactory.getLogger("SuperArchitect");

    @Override
    public void onInitialize() {
        SuperArchitectItems.initialize();
        SuperArchitectBlocks.initialize();
        SuperArchitectItemGroups.initialize();
        SuperArchitectBlockEntities.initialize();
        SuperArchitectMenus.initialize();
        SuperArchitectRecipes.initialize();

        PayloadTypeRegistry.playC2S().register(
                MonitorSearchPacket.TYPE,
                MonitorSearchPacket.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                MonitorSearchPacket.TYPE,
                (payload, context) -> {
                    if (context.player().containerMenu instanceof MonitorMenu menu) {
                        menu.setSearchQuery(payload.query());
                    }
                }
        );

        LOGGER.info("SuperArchitect Initialized!");
    }
}
