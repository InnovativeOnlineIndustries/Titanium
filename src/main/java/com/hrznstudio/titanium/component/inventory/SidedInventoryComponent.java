/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.inventory;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.client.screen.addon.FacingHandlerScreenAddon;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.component.sideness.SidedComponentManager;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class SidedInventoryComponent<T extends IComponentHarness> extends InventoryComponent<T> implements IFacingComponent {

    private int color;
    private int facingHandlerX = 8;
    private int facingHandlerY = 84;
    private HashMap<FacingUtil.Sideness, FaceMode> facingModes;
    private HashMap<FacingUtil.Sideness, Integer> slotCache;
    private int position;
    private boolean hasFacingAddon;
    private FaceMode[] validFaceModes;

    public SidedInventoryComponent(String name, int xPos, int yPos, int size, int position) {
        super(name, xPos, yPos, size);
        this.color = DyeColor.WHITE.getFireworkColor();
        this.facingModes = new HashMap<>();
        this.slotCache = new HashMap<>();
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            this.facingModes.put(value, FaceMode.ENABLED);
        }
        this.position = position;
        this.setColorGuiEnabled(true);
        this.hasFacingAddon = true;
        this.validFaceModes = FaceMode.values();
    }

    public SidedInventoryComponent<T> disableFacingAddon() {
        this.hasFacingAddon = false;
        return this;
    }

    @Override
    public HashMap<FacingUtil.Sideness, FaceMode> getFacingModes() {
        return facingModes;
    }

    @Override
    public int getColor() {
        return new Color(color).getRGB();
    }

    public SidedInventoryComponent<T> setColor(int color) {
        this.color = color;
        return this;
    }

    public SidedInventoryComponent<T> setColor(DyeColor color) {
        this.color = color.getMapColor().col;
        return this;
    }

    @Override
    public Color getColorForSlotRendering(int slot) {
        return getSlotToColorRenderMap().getOrDefault(slot, new Color(color));
    }

    @Override
    public Rectangle getRectangle(IAsset asset) {
        int renderingOffset = 1;
        Rectangle rectangle = new Rectangle(getSlotPosition().apply(0).getLeft() - 1, getSlotPosition().apply(0).getRight() - 1, (int) asset.getArea().getWidth() + getSlotPosition().apply(0).getLeft(), (int) asset.getArea().getHeight() + getSlotPosition().apply(0).getRight());
        for (int i = 0; i < getSlots(); i++) {
            if (getSlotPosition().apply(i).getLeft() < rectangle.getX()) {
                rectangle.setLocation(getSlotPosition().apply(i).getLeft(), rectangle.y);
            }
            if (getSlotPosition().apply(i).getRight() < rectangle.getY()) {
                rectangle.setLocation(rectangle.x, getSlotPosition().apply(i).getRight());
            }
            if (getSlotPosition().apply(i).getLeft() + asset.getArea().getWidth() > rectangle.width) {
                rectangle.setSize(getSlotPosition().apply(i).getLeft() + asset.getArea().width, rectangle.height);
            }
            if (getSlotPosition().apply(i).getRight() + asset.getArea().getHeight() > rectangle.height) {
                rectangle.setSize(rectangle.width, getSlotPosition().apply(i).getRight() + asset.getArea().height);
            }
        }
        return new Rectangle(this.getXPos() - renderingOffset - 2 + rectangle.x, this.getYPos() - renderingOffset - 2 + rectangle.y, rectangle.width + renderingOffset * 2 + 3, rectangle.height + renderingOffset * 2 + 3);
    }

    @Override
    public int getFacingHandlerX() {
        return this.facingHandlerX;
    }

    @Override
    public int getFacingHandlerY() {
        return this.facingHandlerY;
    }

    private boolean workSides(Level level, BlockPos pos, Direction blockFacing, int workAmount, FaceMode mode) {
        for (FacingUtil.Sideness sideness : facingModes.keySet()) {
            if (facingModes.get(sideness) == mode) {
                Direction real = FacingUtil.getFacingFromSide(blockFacing, sideness);
                ResourceHandler<ItemResource> cap = level.getCapability(Capabilities.Item.BLOCK, pos.relative(real), real.getOpposite());
                if (cap != null) {
                    ResourceHandler<ItemResource> from = mode == FaceMode.PUSH ? this : cap;
                    ResourceHandler<ItemResource> to = mode == FaceMode.PUSH ? cap : this;
                    if (ResourceHandlerUtil.move(from, to, resource -> true, workAmount, null) > 0) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean work(Level world, BlockPos pos, Direction blockFacing, int workAmount) {
        if (workSides(world, pos, blockFacing, workAmount, FaceMode.PUSH)) return true;
        return workSides(world, pos, blockFacing, workAmount, FaceMode.PULL);
    }

    @Override
    public SidedInventoryComponent<T> setFacingHandlerPos(int x, int y) {
        this.facingHandlerX = x;
        this.facingHandlerY = y;
        return this;
    }

    @Override
    public FaceMode[] getValidFacingModes() {
        return validFaceModes;
    }

    public SidedInventoryComponent<T> setValidFaceModes(FaceMode... validFaceModes){
        this.validFaceModes = validFaceModes;
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            this.facingModes.put(value, validFaceModes[0]);
        }
        return this;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = com.hrznstudio.titanium.util.ValueIOSerialization.save(provider, this);
        CompoundTag compound = new CompoundTag();
        for (FacingUtil.Sideness facing : facingModes.keySet()) {
            compound.putString(facing.name(), facingModes.get(facing).name());
        }
        nbt.put("FacingModes", compound);
        return nbt;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        com.hrznstudio.titanium.util.ValueIOSerialization.load(provider, nbt, this);
        if (nbt.contains("FacingModes")) {
            CompoundTag compound = nbt.getCompoundOrEmpty("FacingModes");
            for (String face : compound.keySet()) {
                facingModes.put(FacingUtil.Sideness.valueOf(face), FaceMode.valueOf(compound.getStringOr(face, "")));
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = super.getScreenAddons();
        if (hasFacingAddon)
            addons.add(() -> new FacingHandlerScreenAddon(SidedComponentManager.ofRight(getFacingHandlerX(), getFacingHandlerY(), position, AssetTypes.BUTTON_SIDENESS_MANAGER, 4), this, AssetTypes.SLOT, this.getComponentHarness() instanceof ActiveTile ? ((ActiveTile) this.getComponentHarness()).getFacingDirection() : Direction.NORTH));
        return addons;
    }

}
