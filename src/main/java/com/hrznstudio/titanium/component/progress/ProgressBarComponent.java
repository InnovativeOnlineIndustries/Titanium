/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.progress;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.client.screen.addon.ProgressBarScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.container.addon.IntArrayReferenceHolderAddon;
import com.hrznstudio.titanium.container.referenceholder.ProgressBarReferenceHolder;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ProgressBarComponent<T extends IComponentHarness> implements INBTSerializable<CompoundTag>,
        IScreenAddonProvider, IContainerAddonProvider {
    private int posX;
    private int posY;
    private int progress;
    private int maxProgress;
    private int progressIncrease;
    private Predicate<T> canIncrease;
    private Predicate<T> canReset;
    private int tickingTime;
    private Runnable onFinishWork;
    private Runnable onTickWork;
    private Runnable onStart;
    private T componentHarness;
    private BarDirection barDirection;
    private DyeColor color;
    private boolean increaseType;

    public ProgressBarComponent(int posX, int posY, int maxProgress) {
        this.posX = posX;
        this.posY = posY;
        this.progress = 0;
        this.maxProgress = maxProgress;
        this.progressIncrease = 1;
        this.canIncrease = tileEntity -> false;
        this.canReset = tileEntity -> true;
        this.tickingTime = 1;
        this.onFinishWork = () -> {
        };
        this.onTickWork = () -> {
        };
        this.onStart = () -> {
        };
        this.barDirection = BarDirection.VERTICAL_UP;
        this.color = DyeColor.WHITE;
        this.increaseType = true;
    }

    public ProgressBarComponent(int posX, int posY, int progress, int maxProgress) {
        this(posX, posY, maxProgress);
        this.progress = progress;
    }

    /**
     * Sets a runnable to be executed when the bar is completed
     *
     * @param runnable The runnable
     * @return Self
     */
    public ProgressBarComponent<T> setOnFinishWork(Runnable runnable) {
        this.onFinishWork = runnable;
        return this;
    }

    /**
     * Sets a runnable to be executed every time the bar ticks
     *
     * @param runnable The runnable
     * @return Self
     */
    public ProgressBarComponent<T> setOnTickWork(Runnable runnable) {
        this.onTickWork = runnable;
        return this;
    }

    /**
     * Sets a runnable to be executed every time the bar starts from 0
     *
     * @param runnable The runnable
     * @return Self
     */
    public ProgressBarComponent<T> setOnStart(Runnable runnable) {
        this.onStart = runnable;
        return this;
    }

    /**
     * Sets the tile where this bar is running
     *
     * @param componentHarness The tile
     * @return Self
     */
    public ProgressBarComponent<T> setComponentHarness(T componentHarness) {
        this.componentHarness = componentHarness;
        return this;
    }

    /**
     * Gets the tile where this bar is running
     *
     * @return The tile
     */
    public T getComponentHarness() {
        return componentHarness;
    }

    /**
     * Gets if the bar can reset
     *
     * @return True if the bar can be reseted
     */
    public Predicate<T> getCanReset() {
        return canReset;
    }

    /**
     * Sets if the the bar can be reseted when the progress is completed
     *
     * @param canReset A Predicate
     * @return Self
     */
    public ProgressBarComponent<T> setCanReset(Predicate<T> canReset) {
        this.canReset = canReset;
        return this;
    }

    public boolean getIncreaseType() {
        return increaseType;
    }

    /**
     * Changes how the progress bar behaves when working
     *
     * @param increaseType True if the progress bar increases when working, false if the progress bar decreases when working
     * @return itself
     */
    public ProgressBarComponent<T> setIncreaseType(boolean increaseType) {
        this.increaseType = increaseType;
        return this;
    }

    /**
     * Ticks the bar so it can increase if possible, managed by {@link MultiProgressBarHandler#update()}
     */
    public void tickBar() {
        if (componentHarness != null && componentHarness.getComponentWorld().getGameTime() % tickingTime == 0) {
            if (increaseType && progress < maxProgress) {
                setProgress(this.progress + progressIncrease);
                this.onTickWork.run();
            }
            if (!increaseType && progress > 0) {
                setProgress(this.progress - progressIncrease);
                this.onTickWork.run();
            }
        }
        if (increaseType && progress >= maxProgress && canReset.test(componentHarness)) {
            setProgress(0);
            this.onFinishWork.run();
        }
        if (!increaseType && progress <= 0 && canReset.test(componentHarness)) {
            setProgress(maxProgress);
            this.onFinishWork.run();
        }
    }

    /**
     * Gets where the bar is located in the X
     *
     * @return the x position
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Gets where the bar is located in the X
     *
     * @return the y position
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Gets if the progress can be increased
     *
     * @return A predicate
     */
    public Predicate<T> getCanIncrease() {
        return canIncrease;
    }

    /**
     * Sets a predicate to check if the bar can be increased
     *
     * @param canIncrease A predicate
     * @return Self
     */
    public ProgressBarComponent<T> setCanIncrease(Predicate<T> canIncrease) {
        this.canIncrease = canIncrease;
        return this;
    }

    /**
     * Gets the current progress
     *
     * @return The progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Sets the progress bar progress
     *
     * @param progress The progress to set
     */
    public void setProgress(int progress) {
        this.progress = progress;
        if (componentHarness != null) componentHarness.markComponentForUpdate(true);
    }

    /**
     * Gets the max progress of the bar
     *
     * @return The bas progress
     */
    public int getMaxProgress() {
        return maxProgress;
    }

    /**
     * Sets the max progress of the bar
     *
     * @param maxProgress The max progress
     * @return Self
     */
    public ProgressBarComponent<T> setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        return this;
    }

    /**
     * Gets how often the bar ticks
     *
     * @return The tick the bar tries to increase
     */
    public int getTickingTime() {
        return tickingTime;
    }

    /**
     * Sets how often the bar ticks
     *
     * @param tickingTime The ticking time
     * @return Self
     */
    public ProgressBarComponent<T> setTickingTime(int tickingTime) {
        this.tickingTime = tickingTime;
        return this;
    }

    /**
     * Gets how much the bar increases when it can increase progress
     *
     * @return The amount it increases
     */
    public int getProgressIncrease() {
        return progressIncrease;
    }

    /**
     * Sets how much the bar will increase when it can increase
     *
     * @param progressIncrease The increase amount
     * @return Self
     */
    public ProgressBarComponent<T> setProgressIncrease(int progressIncrease) {
        this.progressIncrease = progressIncrease;
        return this;
    }

    /**
     * Gets the bar direction
     *
     * @return The bar direction
     */
    public BarDirection getBarDirection() {
        return barDirection;
    }

    /**
     * Sets the direction render for the bar in the gui
     *
     * @param direction The bar direction
     * @return Self
     */
    public ProgressBarComponent<T> setBarDirection(BarDirection direction) {
        this.barDirection = direction;
        return this;
    }

    /**
     * Gets the color of the bar
     *
     * @return the color
     */
    public DyeColor getColor() {
        return color;
    }

    /**
     * Set the color of the progress bar
     *
     * @param color the color
     * @return Self
     */
    public ProgressBarComponent<T> setColor(DyeColor color) {
        this.color = color;
        return this;
    }

    /**
     * Gets the Gui Addons that it will be added to the machine GUI
     *
     * @return A list of GUI addon factories
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return Collections.singletonList(() -> new ProgressBarScreenAddon<>(posX, posY, this));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("Tick", progress);
        compound.putInt("MaxProgress", maxProgress);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        progress = nbt.getInt("Tick");
        maxProgress = nbt.getInt("MaxProgress");
    }

    public void onStart() {
        onStart.run();
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return Lists.newArrayList(
                () -> new IntArrayReferenceHolderAddon(new ProgressBarReferenceHolder(this))
        );
    }

    public enum BarDirection {
        VERTICAL_UP {
            @Override
            public <T extends IComponentHarness> void render(PoseStack stack, Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarScreenAddon<T> addon) {
                IAsset assetBorder = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL);
                Point offset = assetBorder.getOffset();
                Rectangle area = assetBorder.getArea();
                RenderSystem.setShaderTexture(0, assetBorder.getResourceLocation());
                screen.blit(stack, guiX + addon.getPosX() + offset.x, guiY + addon.getPosY() + offset.y, area.x, area.y, area.width, area.height);
                RenderSystem.setShaderColor(addon.getProgressBar().getColor().getTextureDiffuseColors()[0], addon.getProgressBar().getColor().getTextureDiffuseColors()[1], addon.getProgressBar().getColor().getTextureDiffuseColors()[2], 1);
                IAsset assetBar = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_VERTICAL);
                offset = assetBar.getOffset();
                area = assetBar.getArea();
                RenderSystem.setShaderTexture(0, assetBar.getResourceLocation());
                screen.blit(stack, guiX + addon.getPosX() + offset.x, guiY + addon.getPosY() + offset.y, area.x, area.y, area.width, area.height);
                IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_VERTICAL);
                offset = asset.getOffset();
                area = asset.getArea();
                RenderSystem.setShaderTexture(0, asset.getResourceLocation());
                int progress = addon.getProgressBar().getProgress();
                int maxProgress = addon.getProgressBar().getMaxProgress();
                int progressOffset = progress * area.height / Math.max(maxProgress, 1);
                screen.blit(stack, addon.getPosX() + offset.x + guiX,
                    addon.getPosY() + offset.y + area.height - progressOffset + guiY,
                    area.x,
                    area.y + (area.height - progressOffset),
                    area.width,
                    progressOffset);
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }

            @Override
            public int getXSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL).getArea().getWidth();
            }

            @Override
            public int getYSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL).getArea().getHeight();
            }
        },
        ARROW_RIGHT {
            @Override
            public <T extends IComponentHarness> void render(PoseStack stack, Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarScreenAddon<T> addon) {
                AssetUtil.drawAsset(stack, screen, IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), addon.getPosX() + guiX, addon.getPosY() + guiY);
                IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_ARROW_HORIZONTAL);
                Point offset = asset.getOffset();
                Rectangle area = asset.getArea();
                RenderSystem.setShaderTexture(0, asset.getResourceLocation());
                int progress = addon.getProgressBar().getProgress();
                int maxProgress = addon.getProgressBar().getMaxProgress();
                int progressOffset = progress * area.width / Math.max(maxProgress, 1);
                RenderSystem.setShaderColor(addon.getProgressBar().getColor().getTextureDiffuseColors()[0], addon.getProgressBar().getColor().getTextureDiffuseColors()[1], addon.getProgressBar().getColor().getTextureDiffuseColors()[2], 1);
                screen.blit(stack, addon.getPosX() + offset.x + guiX, addon.getPosY() + offset.y + guiY, area.x, area.y, progressOffset, area.height);
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }

            @Override
            public int getXSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL).getArea().getWidth();
            }

            @Override
            public int getYSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL).getArea().getHeight();
            }
        },

        ARROW_DOWN {
            @Override
            public <T extends IComponentHarness> void render(PoseStack stack, Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarScreenAddon<T> addon) {
                AssetUtil.drawAsset(stack, screen, IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_DOWN), addon.getPosX() + guiX, addon.getPosY() + guiY);
                IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_ARROW_DOWN);
                Point offset = asset.getOffset();
                Rectangle area = asset.getArea();
                RenderSystem.setShaderTexture(0, asset.getResourceLocation());
                int progress = addon.getProgressBar().getProgress();
                int maxProgress = addon.getProgressBar().getMaxProgress();
                int progressOffset = progress * area.height / Math.max(maxProgress, 1);
                RenderSystem.setShaderColor(addon.getProgressBar().getColor().getTextureDiffuseColors()[0], addon.getProgressBar().getColor().getTextureDiffuseColors()[1], addon.getProgressBar().getColor().getTextureDiffuseColors()[2], 1);
                screen.blit(stack, addon.getPosX() + offset.x + guiX, addon.getPosY() + offset.y + guiY, area.x, area.y, area.width, progressOffset);
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }

            @Override
            public int getXSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_DOWN).getArea().getWidth();
            }

            @Override
            public int getYSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_DOWN).getArea().getHeight();
            }
        };

        @OnlyIn(Dist.CLIENT)
        public abstract <T extends IComponentHarness> void render(PoseStack stack, Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarScreenAddon<T> addon);

        @OnlyIn(Dist.CLIENT)
        public abstract int getXSize(IAssetProvider provider);

        @OnlyIn(Dist.CLIENT)
        public abstract int getYSize(IAssetProvider provider);
    }
}
