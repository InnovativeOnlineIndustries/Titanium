package com.hrznstudio.titanium.entity;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.IButtonHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.util.List;

public class LivingEntityHarness implements IContainerAddonProvider, IScreenAddonProvider, IButtonHandler {

    private final LivingEntity entity;
    private final IButtonHandler buttonHandler;
    private final Capability<?>[] capabilities;
    private final IScreenAddonProvider defaultProvider;

    public LivingEntityHarness(LivingEntity entity, IScreenAddonProvider defaultProvider, IButtonHandler buttonHandler, Capability<?>... capabilities) {
        this.entity = entity;
        this.defaultProvider = defaultProvider;
        this.buttonHandler = buttonHandler;
        this.capabilities = capabilities;
    }

    @Nonnull
    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> screenAddons = Lists.newArrayList();
        if (defaultProvider != null) screenAddons.addAll(defaultProvider.getScreenAddons());
        for (Capability<?> capability : capabilities) {
            screenAddons.addAll(entity.getCapability(capability)
                .filter(cap -> cap instanceof IScreenAddonProvider)
                .map(cap -> (IScreenAddonProvider)cap)
                .map(IScreenAddonProvider::getScreenAddons)
                .orElseGet(Lists::newArrayList));
        }
        return screenAddons;
    }

    @Nonnull
    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> containerAddons = Lists.newArrayList();
        for (Capability<?> capability : capabilities) {
            containerAddons.addAll(entity.getCapability(capability)
                .filter(cap -> cap instanceof IContainerAddonProvider)
                .map(cap -> (IContainerAddonProvider)cap)
                .map(IContainerAddonProvider::getContainerAddons)
                .orElseGet(Lists::newArrayList));
        }
        return containerAddons;
    }

    @Override
    public void handleButtonMessage(int id, PlayerEntity playerEntity, CompoundNBT compound) {
        if (buttonHandler != null) {
            buttonHandler.handleButtonMessage(id, playerEntity, compound);
        }
    }
}
