/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.hrznstudio.titanium.module.api.IAlternativeEntries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class DeferredRegistryHelper {

    private final String modId;
    private HashMap<Class<? extends IForgeRegistryEntry>, DeferredRegister<? extends IForgeRegistryEntry>> registries;

    public DeferredRegistryHelper(String modId) {
        this.modId = modId;
        this.registries = new HashMap<>();
    }

    public <T extends IForgeRegistryEntry> DeferredRegister<? super T> addRegistry(Class<T> cl) {
        DeferredRegister deferredRegister = DeferredRegister.create(cl, this.modId);
        deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        //registries.put(cl, deferredRegister);
        return deferredRegister;
    }

    public <T extends IForgeRegistryEntry<? super T>> RegistryObject<T> register(Class<T> cl, String name, Supplier<T> object) {
        DeferredRegister deferredRegister = registries.computeIfAbsent(cl, this::addRegistry);
        Object j = object.get();
        if (j instanceof IAlternativeEntries) { // I'm sorry
            ((IAlternativeEntries) j).addAlternatives(this);
        }
        return deferredRegister.register(name, () -> j);
    }

    public RegistryObject<BlockEntityType<?>> registerBlockEntityType(String name, Supplier<?> object) {
        DeferredRegister deferredRegister = registries.computeIfAbsent(BlockEntityType.class, this::addRegistry);
        return deferredRegister.register(name, object);
    }

    public RegistryObject<EntityType<?>> registerEntityType(String name, Supplier<?> object) {
        DeferredRegister deferredRegister = registries.computeIfAbsent(EntityType.class, this::addRegistry);
        return deferredRegister.register(name, object);
    }

}
