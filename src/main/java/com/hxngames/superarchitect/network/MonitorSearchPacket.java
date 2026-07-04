package com.hxngames.superarchitect.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record MonitorSearchPacket(String query) implements CustomPacketPayload {
    public static final Type<MonitorSearchPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("super_architect", "monitor_search"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MonitorSearchPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            MonitorSearchPacket::query,
            MonitorSearchPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
