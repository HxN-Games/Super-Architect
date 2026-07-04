package com.hxngames.superarchitect.client.datagen.translate;

import com.hxngames.superarchitect.blocks.SuperArchitectBlocks;
import com.hxngames.superarchitect.items.SuperArchitectItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EnglishLangProvider extends FabricLanguageProvider {
    private static SuperArchitectItems items = new SuperArchitectItems();
    private static SuperArchitectBlocks blocks = new SuperArchitectBlocks();

    public EnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }


    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(items.DISK_128, "Disk 128");
        translationBuilder.add(items.DISK_256, "Disk 256");
        translationBuilder.add(items.DISK_512, "Disk 512");
        translationBuilder.add(items.DISK_1k, "Disk 1k");
        translationBuilder.add(items.DISK_2k, "Disk 2k");
        translationBuilder.add(items.DISK_4k, "Disk 4k");
        translationBuilder.add(items.DISK_8k, "Disk 8k");

        translationBuilder.add(blocks.DISK_RACK, "Disk Rack");
        translationBuilder.add(blocks.MONITOR, "Monitor");
        translationBuilder.add(blocks.PIPE, "Network Pipe");

        translationBuilder.add("itemTooltip.super_architect.disk", "Size %1$s/%2$s items");
        translationBuilder.add("itemTooltip.super_architect.disk_full", "Size %1$s/%2$s items (Full)");
        translationBuilder.add("itemgroup.super_architect.main", "Super Architect");
    }


}
