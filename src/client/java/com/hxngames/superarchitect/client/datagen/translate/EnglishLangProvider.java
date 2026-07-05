package com.hxngames.superarchitect.client.datagen.translate;

import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import com.hxngames.superarchitect.items.SuperArchitectItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EnglishLangProvider extends FabricLanguageProvider {
    public EnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }


    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(SuperArchitectItems.DISK_128, "Disk 128");
        translationBuilder.add(SuperArchitectItems.DISK_256, "Disk 256");
        translationBuilder.add(SuperArchitectItems.DISK_512, "Disk 512");
        translationBuilder.add(SuperArchitectItems.DISK_1k, "Disk 1k");
        translationBuilder.add(SuperArchitectItems.DISK_2k, "Disk 2k");
        translationBuilder.add(SuperArchitectItems.DISK_4k, "Disk 4k");
        translationBuilder.add(SuperArchitectItems.DISK_8k, "Disk 8k");
        translationBuilder.add(SuperArchitectItems.CHIP, "Chip");
        translationBuilder.add(SuperArchitectItems.RAW_SILICA, "Raw Silica");
        translationBuilder.add(SuperArchitectItems.SILICON, "Silicon");

        translationBuilder.add(SuperArchitectBlocks.DISK_RACK, "Disk Rack");
        translationBuilder.add(SuperArchitectBlocks.MONITOR, "Monitor");
        translationBuilder.add(SuperArchitectBlocks.BLANK_MONITOR, "Blank Monitor");
        translationBuilder.add(SuperArchitectBlocks.PIPE, "Network Pipe");

        translationBuilder.add("itemTooltip.super_architect.disk", "Size %1$s/%2$s items");
        translationBuilder.add("itemTooltip.super_architect.disk_full", "Size %1$s/%2$s items (Full)");
        translationBuilder.add("itemgroup.super_architect.main", "Super Architect");
    }
}
