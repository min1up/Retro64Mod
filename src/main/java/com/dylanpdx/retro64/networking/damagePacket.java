package com.dylanpdx.retro64.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Damage packet - syncs damage to clients
 */
public class damagePacket {

    Vec3d pos;
    int damageAmount;

    public static final BiConsumer<damagePacket, PacketByteBuf> encoder = new BiConsumer<damagePacket, PacketByteBuf>() {
        @Override
        public void accept(damagePacket dmgPacket, PacketByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeDouble(dmgPacket.pos.x);
            friendlyByteBuf.writeDouble(dmgPacket.pos.y);
            friendlyByteBuf.writeDouble(dmgPacket.pos.z);
            friendlyByteBuf.writeInt(dmgPacket.damageAmount);
        }
    };
    
    public static Function<PacketByteBuf, damagePacket> decoder = new Function<PacketByteBuf, damagePacket>() {
        @Override
        public damagePacket apply(PacketByteBuf friendlyByteBuf) {
            damagePacket dmgPacket = new damagePacket();
            dmgPacket.pos = new Vec3d(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble());
            dmgPacket.damageAmount = friendlyByteBuf.readInt();
            return dmgPacket;
        }
    };

    public damagePacket() {
        pos = new Vec3d(0, 0, 0);
        damageAmount = 0;
    }

    public damagePacket(Vec3d pos, int damageAmount) {
        this.pos = pos;
        this.damageAmount = damageAmount;
    }

}
