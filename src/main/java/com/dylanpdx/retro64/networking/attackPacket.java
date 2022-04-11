package com.dylanpdx.retro64.networking;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

/**
 * Attack packet, used to apply knockback to entities
 */
public class attackPacket {

    int targetID;
    float angle;

    public static final BiConsumer<attackPacket, PacketByteBuf> encoder = new BiConsumer<attackPacket, PacketByteBuf>(){

        @Override
        public void accept(attackPacket attackPacket, PacketByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeInt(attackPacket.targetID);
            friendlyByteBuf.writeFloat(attackPacket.angle);
        }
    };

    public static final Function<PacketByteBuf,attackPacket> decoder = new Function<PacketByteBuf,attackPacket>(){

        @Override
        public attackPacket apply(PacketByteBuf friendlyByteBuf) {
            attackPacket attackPacket = new attackPacket(friendlyByteBuf.readInt(), friendlyByteBuf.readFloat());
            return attackPacket;
        }
    };

    public attackPacket(int eID, float ang){
        this.targetID = eID;
        this.angle = ang;
    }

    public static void applyKnockback(Entity e, float ang){
        // convert angle, which is in radians to Vec3d
        ang= (float) (Math.toRadians(90)-ang);
        Vec3d knockback = new Vec3d(Math.cos(ang), .7f, Math.sin(ang));
        e.setVelocity(e.getVelocity().add(new Vec3d(knockback.getX(),knockback.getY(),knockback.getZ())));

    }
}
