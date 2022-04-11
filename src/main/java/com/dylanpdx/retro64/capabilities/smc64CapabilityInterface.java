package com.dylanpdx.retro64.capabilities;

import net.minecraft.nbt.NbtCompound;

//INBTSerializable is annoying.
//The functions get overwritten anyway.
public interface smc64CapabilityInterface {

    boolean getIsEnabled();
    void setIsEnabled(boolean isEnabled);
    NbtCompound serializeNBT();
    void deserializeNBT(NbtCompound nbt);
}
