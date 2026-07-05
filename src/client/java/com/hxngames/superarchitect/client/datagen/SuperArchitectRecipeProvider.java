package com.hxngames.superarchitect.client.datagen;

import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import com.hxngames.superarchitect.items.SuperArchitectItems;
import com.hxngames.superarchitect.recipes.DiskUpgradeRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectItems.CHIP, 1)
                .pattern("gsg")
                .pattern("srs")
                .pattern("gsg")
                .define('g', Items.GOLD_INGOT)
                .define('s', SuperArchitectItems.SILICON)
                .define('r', Items.REDSTONE)
                .group("multi_bench")
                .unlockedBy(getHasName(SuperArchitectItems.SILICON), has(SuperArchitectItems.SILICON))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectBlocks.DISK_RACK, 1)
                .pattern("ipi")
                .pattern("scs")
                .pattern("ipi")
                .define('i', Items.IRON_INGOT)
                .define('s', SuperArchitectItems.SILICON)
                .define('p', SuperArchitectBlocks.PIPE)
                .define('c', SuperArchitectItems.CHIP)
                .group("multi_bench")
                .unlockedBy(getHasName(SuperArchitectItems.SILICON), has(SuperArchitectItems.SILICON))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectBlocks.PIPE, 4)
                .pattern("ss")
                .pattern("ss")
                .define('s', SuperArchitectItems.SILICON)
                .group("multi_bench")
                .unlockedBy(getHasName(SuperArchitectItems.SILICON), has(SuperArchitectItems.SILICON))
                .save(exporter);

        RecipeOutput upgradeExporter = new RecipeOutput() {
            @Override
            public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {
                if (recipe instanceof ShapedRecipe shaped) {
                    DiskUpgradeRecipe upgradeRecipe = new DiskUpgradeRecipe(shaped);
                    exporter.accept(id, upgradeRecipe, advancement);
                } else {
                    exporter.accept(id, recipe, advancement);
                }
            }

            @Override
            public Advancement.@NotNull Builder advancement() {
                return exporter.advancement();
            }
        };

        // Disk 128 -> 256
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectItems.DISK_256, 1)
                .pattern("sms")
                .pattern("mdm")
                .pattern("sms")
                .define('s', SuperArchitectItems.SILICON)
                .define('m', Items.COPPER_INGOT)
                .define('d', SuperArchitectItems.DISK_128)
                .unlockedBy(getHasName(SuperArchitectItems.DISK_128), has(SuperArchitectItems.DISK_128))
                .save(upgradeExporter);

        // Disk 256 -> 512
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectItems.DISK_512, 1)
                .pattern("sms")
                .pattern("mdm")
                .pattern("sms")
                .define('s', SuperArchitectItems.SILICON)
                .define('m', Items.IRON_INGOT)
                .define('d', SuperArchitectItems.DISK_256)
                .unlockedBy(getHasName(SuperArchitectItems.DISK_256), has(SuperArchitectItems.DISK_256))
                .save(upgradeExporter);

        // Disk 512 -> 1k
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectItems.DISK_1k, 1)
                .pattern("sms")
                .pattern("mdm")
                .pattern("sms")
                .define('s', SuperArchitectItems.SILICON)
                .define('m', Items.GOLD_INGOT)
                .define('d', SuperArchitectItems.DISK_512)
                .unlockedBy(getHasName(SuperArchitectItems.DISK_512), has(SuperArchitectItems.DISK_512))
                .save(upgradeExporter);

        // Disk 1k -> 2k
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectItems.DISK_2k, 1)
                .pattern("sms")
                .pattern("mdm")
                .pattern("sms")
                .define('s', SuperArchitectItems.SILICON)
                .define('m', Items.EMERALD)
                .define('d', SuperArchitectItems.DISK_1k)
                .unlockedBy(getHasName(SuperArchitectItems.DISK_1k), has(SuperArchitectItems.DISK_1k))
                .save(upgradeExporter);

        // Disk 2k -> 4k
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectItems.DISK_4k, 1)
                .pattern("sms")
                .pattern("mdm")
                .pattern("sms")
                .define('s', SuperArchitectItems.SILICON)
                .define('m', Items.DIAMOND)
                .define('d', SuperArchitectItems.DISK_2k)
                .unlockedBy(getHasName(SuperArchitectItems.DISK_2k), has(SuperArchitectItems.DISK_2k))
                .save(upgradeExporter);

        // Disk 4k -> 8k
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(SuperArchitectItems.DISK_4k),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.MISC,
                        SuperArchitectItems.DISK_8k
                )
                .unlocks(getHasName(SuperArchitectItems.DISK_4k), has(SuperArchitectItems.DISK_4k))
                .save(exporter, getConversionRecipeName(SuperArchitectItems.DISK_8k, SuperArchitectItems.DISK_4k) + "_smithing");

        // Base Disk 128
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectItems.DISK_128, 1)
                .pattern("srs")
                .pattern("rcr")
                .pattern("srs")
                .define('s', SuperArchitectItems.SILICON)
                .define('r', Items.REDSTONE)
                .define('c', SuperArchitectItems.CHIP)
                .unlockedBy(getHasName(SuperArchitectItems.CHIP), has(SuperArchitectItems.CHIP))
                .save(exporter);
        // Blank Monitor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectBlocks.BLANK_MONITOR, 1)
                .pattern("ggg")
                .pattern("gsg")
                .pattern("ggg")
                .define('g', Items.GLASS)
                .define('s', SuperArchitectItems.SILICON)
                .unlockedBy(getHasName(SuperArchitectItems.SILICON), has(SuperArchitectItems.SILICON))
                .save(exporter);

        // Monitor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SuperArchitectBlocks.MONITOR, 1)
                .pattern("cgc")
                .pattern("gbg")
                .pattern("cgc")
                .define('c', SuperArchitectItems.CHIP)
                .define('g', Items.GOLD_INGOT)
                .define('b', SuperArchitectBlocks.BLANK_MONITOR)
                .unlockedBy(getHasName(SuperArchitectBlocks.BLANK_MONITOR), has(SuperArchitectBlocks.BLANK_MONITOR))
                .save(exporter);
    }
}
