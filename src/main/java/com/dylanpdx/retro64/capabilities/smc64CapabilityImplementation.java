package com.dylanpdx.retro64.capabilities;

import net.minecraft.nbt.NbtCompound;

public class smc64CapabilityImplementation implements smc64CapabilityInterface {

    private static final String NBT_KEY_ENABLED = "smc64_enabled";

    private boolean isEnabled;

    @Override
    public boolean getIsEnabled() {
        return isEnabled;
    }

    @Override
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public NbtCompound serializeNBT() {
        final NbtCompound tag = new CompoundTag();
        tag.putBoolean(NBT_KEY_ENABLED, this.isEnabled);
        return tag;
    }

    @Override
    public void deserializeNBT(NbtCompound nbt) {
        this.isEnabled = nbt.getBoolean(NBT_KEY_ENABLED);
    }
}
