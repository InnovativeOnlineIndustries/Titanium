package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.entity.LivingEntityHarnessRegistry;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

public class LivingEntityLocatorInstance extends LocatorInstance {

    private int entityID;

    public LivingEntityLocatorInstance() {
        this(null);
    }

    public LivingEntityLocatorInstance(LivingEntity livingEntity) {
        super(LocatorTypes.LIVING_ENTITY);
        if (livingEntity != null) {
            this.entityID = livingEntity.getEntityId();
        }
    }

    @Nonnull
    @Override
    public Optional<?> locale(PlayerEntity playerEntity) {
        return Optional.of(playerEntity.getEntityWorld().getEntityByID(entityID)).flatMap(entity -> LivingEntityHarnessRegistry.createLivingEntityHarness((LivingEntity) entity));
    }

}
