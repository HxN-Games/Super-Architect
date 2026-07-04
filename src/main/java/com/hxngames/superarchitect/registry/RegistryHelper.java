package com.hxngames.superarchitect.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RegistryHelper {

    /**
     * Registers an Item to the Minecraft item registry.
     *
     * @param modId The Mod ID registering the item.
     * @param name  The name (path) of the item.
     * @param item  The item instance to register.
     * @return The registered item.
     */
    public static <T extends Item> T registerItem(String modId, String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, name), item);
    }

    /**
     * Registers a Block to the Minecraft block registry.
     *
     * @param modId The Mod ID registering the block.
     * @param name  The name (path) of the block.
     * @param block The block instance to register.
     * @return The registered block.
     */
    public static <T extends Block> T registerBlock(String modId, String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, name), block);
    }

    /**
     * Registers a Block and its corresponding BlockItem to the registries.
     *
     * @param modId          The Mod ID registering the block and item.
     * @param name           The name (path) of the block/item.
     * @param block          The block instance to register.
     * @param itemProperties The item properties for the BlockItem.
     * @return The registered block.
     */
    public static <T extends Block> T registerBlockWithItem(String modId, String name, T block, Item.Properties itemProperties) {
        T registeredBlock = registerBlock(modId, name, block);
        registerItem(modId, name, new BlockItem(registeredBlock, itemProperties));
        return registeredBlock;
    }

    /**
     * Registers a BlockEntityType to the Minecraft block entity type registry.
     *
     * @param modId           The Mod ID registering the block entity type.
     * @param name            The name (path) of the block entity type.
     * @param blockEntityType The block entity type instance to register.
     * @return The registered block entity type.
     */
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String modId, String name, BlockEntityType<T> blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), blockEntityType);
    }

    /**
     * Registers a DataComponentType to the Minecraft data component type registry.
     *
     * @param modId The Mod ID registering the component type.
     * @param name  The name (path) of the component type.
     * @param type  The data component type instance to register.
     * @return The registered data component type.
     */
    public static <T> net.minecraft.core.component.DataComponentType<T> registerDataComponentType(String modId, String name, net.minecraft.core.component.DataComponentType<T> type) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), type);
    }


    /**
     * Gets the ID (path) of an Item from the registry.
     * Useful for Datagen to easily get just the item's name.
     *
     * @param item The Item instance.
     * @return The ID (path) string of the Item.
     */
    public static String getId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    /**
     * Gets the ID (path) of a Block from the registry.
     * Useful for Datagen to easily get just the block's name.
     *
     * @param block The Block instance.
     * @return The ID (path) string of the Block.
     */
    public static String getId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    /**
     * Gets the full ResourceLocation of an Item from the registry.
     *
     * @param item The Item instance.
     * @return The ResourceLocation of the Item.
     */
    public static ResourceLocation getIdentifier(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    /**
     * Gets the full ResourceLocation of a Block from the registry.
     *
     * @param block The Block instance.
     * @return The ResourceLocation of the Block.
     */
    public static ResourceLocation getIdentifier(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}

