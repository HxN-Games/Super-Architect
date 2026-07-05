package com.hxngames.superarchitect.recipes;

import com.hxngames.superarchitect.SuperArchitect;
import com.hxngames.superarchitect.registry.RegistryHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class SuperArchitectRecipes {

    public static final RecipeSerializer<DiskUpgradeRecipe> DISK_UPGRADE_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            ResourceLocation.fromNamespaceAndPath(SuperArchitect.MOD_ID, "disk_upgrade"),
            new RecipeSerializer<DiskUpgradeRecipe>() {
                @Override
                public MapCodec<DiskUpgradeRecipe> codec() {
                    return ShapedRecipe.Serializer.CODEC.xmap(
                            DiskUpgradeRecipe::new,
                            DiskUpgradeRecipe::getWrapped
                    );
                }

                @Override
                public StreamCodec<RegistryFriendlyByteBuf, DiskUpgradeRecipe> streamCodec() {
                    return ShapedRecipe.Serializer.STREAM_CODEC.map(
                            DiskUpgradeRecipe::new,
                            DiskUpgradeRecipe::getWrapped
                    );
                }
            }
    );

    public static void initialize() {
        // Classloading trigger
    }
}
