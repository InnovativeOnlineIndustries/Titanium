package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Hand;

import java.util.Optional;

public class HeldStackLocatorInstance extends LocatorInstance {
    private boolean mainHand;

    public HeldStackLocatorInstance() {
        super(LocatorTypes.HELD_STACK);
    }

    public HeldStackLocatorInstance(boolean mainHand) {
        super(LocatorTypes.HELD_STACK);
        this.mainHand = mainHand;
    }

    @Override
    public Optional<?> locale(PlayerEntity playerEntity) {
        return Optional.of(playerEntity.getHeldItem(mainHand ? Hand.MAIN_HAND : Hand.OFF_HAND));
    }
}
