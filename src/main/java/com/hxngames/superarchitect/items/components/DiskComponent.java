package com.hxngames.superarchitect.items.components;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record DiskComponent(List<ItemEntry> items) {
    
    public record ItemEntry(ItemStack stack, int count) {
        public static final Codec<ItemEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.CODEC.fieldOf("item").forGetter(ItemEntry::stack),
                Codec.INT.fieldOf("count").forGetter(ItemEntry::count)
        ).apply(instance, ItemEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemEntry> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, ItemEntry::stack,
                ByteBufCodecs.INT, ItemEntry::count,
                ItemEntry::new
        );
    }

    public static final Codec<DiskComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemEntry.CODEC.listOf().fieldOf("items").forGetter(DiskComponent::items)
    ).apply(instance, DiskComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DiskComponent> STREAM_CODEC = StreamCodec.composite(
            ItemEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), DiskComponent::items,
            DiskComponent::new
    );
}
