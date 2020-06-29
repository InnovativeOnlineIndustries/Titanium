package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.entity.LivingEntityHarnessRegistry;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class LivingEntityLocatorInstance extends LocatorInstance {

    private LivingEntity livingEntity;

    public LivingEntityLocatorInstance() {
        this(null);
    }

    public LivingEntityLocatorInstance(LivingEntity livingEntity) {
        super(LocatorTypes.LIVING_ENTITY);
        this.livingEntity = livingEntity;
    }

    @Override
    public Optional<?> locale(PlayerEntity playerEntity) {
        if (livingEntity == null) return Optional.empty();
        return Optional.ofNullable(playerEntity.getEntityWorld().getEntityByID(livingEntity.getEntityId())).map(entity -> LivingEntityHarnessRegistry.createLivingEntityHarness((LivingEntity) entity)).orElse(null);
    }

    public boolean isAlive() {
        return livingEntity.isAlive();
    }
}
