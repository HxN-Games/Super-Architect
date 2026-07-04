package com.hxngames.superarchitect.client;

import com.hxngames.superarchitect.client.datagen.ModelProvider;
import com.hxngames.superarchitect.client.datagen.SuperArchitectRecipeProvider;
import com.hxngames.superarchitect.client.datagen.translate.EnglishLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SuperArchitectDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(EnglishLangProvider::new);
        pack.addProvider(ModelProvider::new);
        pack.addProvider(SuperArchitectRecipeProvider::new);
    }
}
