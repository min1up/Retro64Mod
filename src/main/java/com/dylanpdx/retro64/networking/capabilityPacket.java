package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.capabilities.smc64CapabilityImplementation;
import com.dylanpdx.retro64.capabilities.smc64CapabilityInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Capability packet - Syncs capability data between client and server
 */
public class capabilityPacket {

    smc64CapabilityInterface capability;
    UUID pUUID;
    boolean kill;


    public PlayerEntity getPlayer(){
        return MinecraftClient.getInstance().world.getPlayerByUuid(pUUID);
    }

    public void setPlayer(PlayerEntity player){
        this.pUUID=player.getUuid();
    }

    public static final BiConsumer<capabilityPacket, PacketByteBuf> encoder = new BiConsumer<capabilityPacket, PacketByteBuf>() {
        @Override
        public void accept(capabilityPacket capabilityPacket, PacketByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeNbt(capabilityPacket.capability.serializeNBT());
            friendlyByteBuf.writeBoolean(capabilityPacket.pUUID!=null);
            if(capabilityPacket.pUUID!=null){
                friendlyByteBuf.writeUuid(capabilityPacket.pUUID);
            }
            friendlyByteBuf.writeBoolean(capabilityPacket.kill);
        }
    };

    public static Function<PacketByteBuf, capabilityPacket> decoder = new Function<PacketByteBuf, capabilityPacket>() {
        @Override
        public capabilityPacket apply(PacketByteBuf friendlyByteBuf) {
            var cpacket = new capabilityPacket();
            cpacket.capability = new smc64CapabilityImplementation();
            cpacket.capability.deserializeNBT(friendlyByteBuf.readNbt());
            boolean hasPlayer = friendlyByteBuf.readBoolean();
            if(hasPlayer){
                cpacket.pUUID = friendlyByteBuf.readUuid();
            }
            cpacket.kill = friendlyByteBuf.readBoolean();
            return cpacket;
        }
    };

    public capabilityPacket(smc64CapabilityInterface capability, PlayerEntity p){
        this.capability=capability;
        setPlayer(p);
    }

    public capabilityPacket(smc64CapabilityInterface capability,UUID pUUID){
        this.capability=capability;
        this.pUUID=pUUID;
    }

    public capabilityPacket(smc64CapabilityInterface capability,boolean kill){
        this.capability=capability;
        this.kill=kill;
    }

    capabilityPacket(){

    }
}
