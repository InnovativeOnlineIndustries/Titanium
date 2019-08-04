package com.hrznstudio.titanium.reward.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public class EnabledRewards implements INBTSerializable<CompoundNBT> {

    private HashMap<ResourceLocation, String> enabled;

    public EnabledRewards() {
        this.enabled = new HashMap<>();
    }

    public HashMap<ResourceLocation, String> getEnabled() {
        return enabled;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        enabled.forEach((resourceLocation, s) -> compoundNBT.putString(resourceLocation.toString(), s));
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        enabled.clear();
        nbt.keySet().forEach(s -> enabled.put(new ResourceLocation(s), nbt.getString(s)));
    }
}
