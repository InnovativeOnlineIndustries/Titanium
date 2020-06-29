package com.hrznstudio.titanium.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class LivingEntityHarnessRegistry {

    private static final LivingEntityHarnessRegistry INSTANCE = new LivingEntityHarnessRegistry();

    private final Map<Item, Function<LivingEntity, LivingEntityHarness>> harnessCreators;

    public LivingEntityHarnessRegistry() {
        harnessCreators = Maps.newHashMap();
    }


    public static void register(Item item, Function<LivingEntity, LivingEntityHarness> harnessCreator) {
        getHarnessCreators().put(item, harnessCreator);
    }

    public static Map<Item, Function<LivingEntity, LivingEntityHarness>> getHarnessCreators() {
        return getInstance().harnessCreators;
    }

    public static Optional<LivingEntityHarness> createLivingEntityHarness(LivingEntity livingEntity) {
        return Optional.of(livingEntity.getType())
            .map(getHarnessCreators()::get)
            .map(harnessCreator -> harnessCreator.apply(livingEntity));
    }

    public static LivingEntityHarnessRegistry getInstance() {
        return INSTANCE;
    }

}
