package com.hrznstudio.titanium.module;

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
        registries.put(cl, deferredRegister);
        return deferredRegister;
    }

    public <T extends IForgeRegistryEntry<? super T>> RegistryObject<T> register(Class<T> cl, String name, Supplier<T> object) {
        DeferredRegister deferredRegister = registries.computeIfAbsent(cl, this::addRegistry);
        return deferredRegister.register(name, object);
    }

    public RegistryObject<BlockEntityType<?>> registerBlockEntityType(String name, Supplier<?> object) {
        DeferredRegister deferredRegister = registries.computeIfAbsent(BlockEntityType.class, this::addRegistry);
        return deferredRegister.register(name, object);
    }
}
