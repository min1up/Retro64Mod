package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.sm64.libsm64.AnimInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * MChar Packet - Syncs player position, animation, and model from client to server
 * The server -> client version is in modelPacket
 */
public class mCharPacket {

    Vec3d pos;
    AnimInfo animInfo;
    public short animXRot,animYRot,animZRot;
    public int action,model;
    public PlayerEntity player;

    public static final BiConsumer<mCharPacket, PacketByteBuf> encoder = new BiConsumer<mCharPacket, PacketByteBuf>(){
        @Override
        public void accept(mCharPacket mCharPacket, PacketByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeDouble(mCharPacket.pos.getX());
            friendlyByteBuf.writeDouble(mCharPacket.pos.getY());
            friendlyByteBuf.writeDouble(mCharPacket.pos.getZ());
            try {
                friendlyByteBuf.writeByteArray(mCharPacket.animInfo.serialize());
            } catch (IOException e) {
                e.printStackTrace();
            }
            friendlyByteBuf.writeShort(mCharPacket.animXRot);
            friendlyByteBuf.writeShort(mCharPacket.animYRot);
            friendlyByteBuf.writeShort(mCharPacket.animZRot);
            friendlyByteBuf.writeInt(mCharPacket.action);
            friendlyByteBuf.writeInt(mCharPacket.model);
        }
    };

    public static final Function<PacketByteBuf, mCharPacket> decoder = new Function<PacketByteBuf, mCharPacket>(){

        @Override
        public mCharPacket apply(PacketByteBuf friendlyByteBuf) {
            mCharPacket mp = new mCharPacket();
            mp.pos = new Vec3d(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble());
            byte[] bytes = friendlyByteBuf.readByteArray();
            try {
                mp.animInfo = AnimInfo.deserialize(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.animXRot = friendlyByteBuf.readShort();
            mp.animYRot = friendlyByteBuf.readShort();
            mp.animZRot = friendlyByteBuf.readShort();
            mp.action = friendlyByteBuf.readInt();
            mp.model = friendlyByteBuf.readInt();
            return mp;
        }
    };

    public mCharPacket() {
        this.pos = new Vec3d(0, 0, 0);
        this.animInfo = null;

    }

    public mCharPacket(Vec3d pos, AnimInfo animInfo,short animXRot,short animYRot,short animZRot,int action,int model,PlayerEntity player) {
        this.pos = pos;
        this.animInfo = animInfo;
        this.animXRot = animXRot;
        this.animYRot = animYRot;
        this.animZRot = animZRot;
        this.player = player;
        this.action = action;
        this.model = model;
    }

}
