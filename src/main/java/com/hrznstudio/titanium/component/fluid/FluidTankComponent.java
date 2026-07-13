/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.fluid;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.client.assets.types.ITankAsset;
import com.hrznstudio.titanium.client.screen.addon.TankScreenAddon;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.container.addon.IntArrayReferenceHolderAddon;
import com.hrznstudio.titanium.container.referenceholder.FluidTankReferenceHolder;
import com.hrznstudio.titanium.nbthandler.INBTSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.List;

public class FluidTankComponent<T extends IComponentHarness> extends FluidStacksResourceHandler implements IScreenAddonProvider,
        IContainerAddonProvider, INBTSerializable<CompoundTag> {

    private final int posX;
    private final int posY;
    private String name;
    private T componentHarness;
    private Type tankType;
    private Action tankAction;
    private Runnable onContentChange;

    public FluidTankComponent(String name, int amount, int posX, int posY) {
        super(1, amount);
        this.posX = posX;
        this.posY = posY;
        this.name = name;
        this.tankType = Type.NORMAL;
        this.tankAction = Action.BOTH;
        this.onContentChange = () -> {
        };
    }

    /**
     * Sets the tile to be automatically marked dirty when the contents change
     *
     * @param componentHarness The tile where the tank is
     * @return itself
     */
    public FluidTankComponent<T> setComponentHarness(T componentHarness) {
        this.componentHarness = componentHarness;
        return this;
    }

    public T getComponentHarness() {
        return componentHarness;
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        if (componentHarness != null) {
            componentHarness.markComponentForUpdate(true);
        }
        onContentChange.run();
    }

    public String getName() {
        return name;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Type getTankType() {
        return tankType;
    }

    public FluidTankComponent<T> setTankType(Type tankType) {
        this.tankType = tankType;
        return this;
    }

    public FluidTankComponent<T> setOnContentChange(Runnable onContentChange) {
        this.onContentChange = onContentChange;
        return this;
    }

    public Action getTankAction() {
        return tankAction;
    }

    public FluidTankComponent<T> setTankAction(Action tankAction) {
        this.tankAction = tankAction;
        return this;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return getTankAction().canFill() ? super.insert(index, resource, amount, transaction) : 0;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return getTankAction().canDrain() ? super.extract(index, resource, amount, transaction) : 0;
    }

    public FluidStack getFluid() {
        return FluidUtil.getStack(this, 0);
    }

    public int getFluidAmount() {
        return getAmountAsInt(0);
    }

    public int getCapacity() {
        return getCapacityAsInt(0, FluidResource.EMPTY);
    }

    public int insertForced(FluidResource resource, int amount, TransactionContext transaction) {
        return super.insert(0, resource, amount, transaction);
    }

    public int extractForced(FluidResource resource, int amount, TransactionContext transaction) {
        return super.extract(0, resource, amount, transaction);
    }

    public boolean isFluidValid(FluidStack stack) {
        return !stack.isEmpty() && isValid(0, FluidResource.of(stack));
    }

    public void setFluidStack(FluidStack fluidStack) {
        set(0, FluidResource.of(fluidStack), fluidStack.getAmount());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        addons.add(() -> new TankScreenAddon(posX, posY, this, tankType));
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return Lists.newArrayList(
                () -> new IntArrayReferenceHolderAddon(new FluidTankReferenceHolder(this))
        );
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return com.hrznstudio.titanium.util.ValueIOSerialization.save(provider, this);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        com.hrznstudio.titanium.util.ValueIOSerialization.load(provider, nbt, this);
    }

    public enum Type {
        NORMAL(AssetTypes.TANK_NORMAL),
        SMALL(AssetTypes.TANK_SMALL);

        private final IAssetType<ITankAsset> assetType;

        Type(IAssetType<ITankAsset> assetType) {
            this.assetType = assetType;
        }

        public IAssetType<ITankAsset> getAssetType() {
            return assetType;
        }
    }

    public enum Action {
        FILL(true, false),
        DRAIN(false, true),
        BOTH(true, true),
        NONE(false, false);

        private final boolean fill;
        private final boolean drain;

        Action(boolean fill, boolean drain) {
            this.fill = fill;
            this.drain = drain;
        }

        public boolean canFill() {
            return fill;
        }

        public boolean canDrain() {
            return drain;
        }
    }
}
