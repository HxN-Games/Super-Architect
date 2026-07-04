package com.hxngames.superarchitect.client.datagen;

import com.hxngames.superarchitect.items.SuperArchitectItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class SuperArchitectRecipeProvider extends FabricRecipeProvider {
    public SuperArchitectRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        // 1 Nether Quartz -> 1 Raw Silica
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SuperArchitectItems.RAW_SILICA, 1)
                .requires(Items.QUARTZ)
                .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                .save(exporter);

        // Blast Furnace: Raw Silica -> Silicon
        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(SuperArchitectItems.RAW_SILICA),
                RecipeCategory.MISC,
                SuperArchitectItems.SILICON,
                0.7f,
                200 // 10 seconds
        )
        .unlockedBy(getHasName(SuperArchitectItems.RAW_SILICA), has(SuperArchitectItems.RAW_SILICA))
        .save(exporter, getConversionRecipeName(SuperArchitectItems.SILICON, SuperArchitectItems.RAW_SILICA) + "_blasting");
    }
}
