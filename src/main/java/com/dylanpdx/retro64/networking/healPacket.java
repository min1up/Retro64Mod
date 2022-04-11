package com.dylanpdx.retro64.networking;

import net.minecraft.network.PacketByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Heal packet - Syncs when a player should heal
 */
public class healPacket {
    
    byte healAmount;

    public static final BiConsumer<healPacket, PacketByteBuf> encoder = new BiConsumer<healPacket, PacketByteBuf>() {
        @Override
        public void accept(healPacket dmgPacket, PacketByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeByte(dmgPacket.healAmount);
        }
    };

    public static Function<PacketByteBuf, healPacket> decoder = new Function<PacketByteBuf, healPacket>() {
        @Override
        public healPacket apply(PacketByteBuf friendlyByteBuf) {
            healPacket dmgPacket = new healPacket();
            dmgPacket.healAmount = friendlyByteBuf.readByte();
            return dmgPacket;
        }
    };

    private healPacket() {
        healAmount = 0;
    }

    public healPacket(byte healAmount) {
        this.healAmount = healAmount;
    }

}

