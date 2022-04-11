package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.sm64.libsm64.AnimInfo;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * model packet, syncs information about the player's model/animation from server -> client
 *
 */
public class modelPacket {

    public AnimInfo animInfo;
    public short animXRot,animYRot,animZRot;
    public int action,model;
    public Vec3d pos;
    public UUID user;

    public static final BiConsumer<modelPacket, PacketByteBuf> encoder = new BiConsumer<modelPacket, PacketByteBuf>() {
        @Override
        public void accept(modelPacket modelPacket, PacketByteBuf friendlyBytebuf) {
            friendlyBytebuf.writeUuid(modelPacket.user);
            try {
                friendlyBytebuf.writeByteArray(modelPacket.animInfo.serialize());
            } catch (IOException e) {
                e.printStackTrace();
            }
            friendlyBytebuf.writeShort(modelPacket.animXRot);
            friendlyBytebuf.writeShort(modelPacket.animYRot);
            friendlyBytebuf.writeShort(modelPacket.animZRot);
            friendlyBytebuf.writeInt(modelPacket.action);
            friendlyBytebuf.writeInt(modelPacket.model);
            friendlyBytebuf.writeDouble(modelPacket.pos.x);
            friendlyBytebuf.writeDouble(modelPacket.pos.y);
            friendlyBytebuf.writeDouble(modelPacket.pos.z);
        }
    };


    public static Function<PacketByteBuf, modelPacket> decoder = new Function<PacketByteBuf, modelPacket>() {
        @Override
        public modelPacket apply(PacketByteBuf friendlyByteBuf) {
            modelPacket modelPacket = new modelPacket();
            modelPacket.user = friendlyByteBuf.readUuid();
            try {
                modelPacket.animInfo = AnimInfo.deserialize(friendlyByteBuf.readByteArray());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            modelPacket.animXRot = friendlyByteBuf.readShort();
            modelPacket.animYRot = friendlyByteBuf.readShort();
            modelPacket.animZRot = friendlyByteBuf.readShort();
            modelPacket.action = friendlyByteBuf.readInt();
            modelPacket.model = friendlyByteBuf.readInt();
            modelPacket.pos = new Vec3d(friendlyByteBuf.readDouble(),friendlyByteBuf.readDouble(),friendlyByteBuf.readDouble());

            return modelPacket;
        }
    };

    public modelPacket(){
        animInfo=null;
    }

    public modelPacket(AnimInfo animInfo,short animXRot,short animYRot,short animZRot,int action,int model,Vec3d pos, UUID user){
        this.user = user;
        this.animInfo = animInfo;
        this.animXRot = animXRot;
        this.animYRot = animYRot;
        this.animZRot = animZRot;
        this.action = action;
        this.model = model;
        this.pos=pos;
    }

}
