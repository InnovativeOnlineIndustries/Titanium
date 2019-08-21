package com.hrznstudio.titanium.client.gui.container;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.client.gui.IGuiAddonConsumer;
import com.hrznstudio.titanium.client.gui.addon.interfaces.ICanMouseDrag;
import com.hrznstudio.titanium.client.gui.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class GuiContainerBase<T extends Container> extends ContainerScreen<T> implements IGuiAddonConsumer {
    private final T container;
    private final ITextComponent title;
    private IAssetProvider assetProvider;
    private int xCenter;
    private int yCenter;
    private List<IGuiAddon> addons;

    private int dragX;
    private int dragY;

    public GuiContainerBase(T container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.container = container;
        this.title = title;
        this.assetProvider = IAssetProvider.DEFAULT_PROVIDER;
        IAsset background = IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND);
        this.xSize = background.getArea().width;
        this.ySize = background.getArea().height;
        this.addons = new ArrayList<>();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.renderBackground();
        xCenter = (width - xSize) / 2;
        yCenter = (height - ySize) / 2;
        //BG RENDERING
        GlStateManager.color4f(1, 1, 1, 1);
        getMinecraft().getTextureManager().bindTexture(IAssetProvider.getAsset(assetProvider, AssetTypes.BACKGROUND).getResourceLocation());
        blit(xCenter, yCenter, 0, 0, xSize, ySize);
        drawCenteredString(Minecraft.getInstance().fontRenderer, TextFormatting.GRAY + title.getFormattedText(), xCenter + xSize / 2, yCenter + 6, 0xFFFFFF);
        this.checkForMouseDrag(mouseX, mouseY);
        addons.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerBackgroundLayer(this, assetProvider, xCenter, yCenter, mouseX, mouseY, partialTicks));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        addons.forEach(iGuiAddon -> iGuiAddon.drawGuiContainerForegroundLayer(this, assetProvider, xCenter, yCenter, mouseX, mouseY));
        renderHoveredToolTip(mouseX - xCenter, mouseY - yCenter);
        for (IGuiAddon iGuiAddon : addons) {
            if (iGuiAddon.isInside(this, mouseX - xCenter, mouseY - yCenter) && !iGuiAddon.getTooltipLines().isEmpty()) {
                renderTooltip(iGuiAddon.getTooltipLines(), mouseX - xCenter, mouseY - yCenter);
            }
        }
    }

    private void checkForMouseDrag(int mouseX, int mouseY) {
        if (GLFW.glfwGetMouseButton(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            for (IGuiAddon iGuiAddon : this.addons) {
                if (iGuiAddon instanceof ICanMouseDrag /*&& iGuiAddon.isInside(null, mouseX - x, mouseY - y)*/) {
                    ((ICanMouseDrag) iGuiAddon).drag(mouseX - dragX, mouseY - dragY);
                }
            }
        }
        this.dragX = mouseX;
        this.dragY = mouseY;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        new ArrayList<>(addons).stream().filter(iGuiAddon -> iGuiAddon instanceof IClickable && iGuiAddon.isInside(this, mouseX - xCenter, mouseY - yCenter))
                .forEach(iGuiAddon -> ((IClickable) iGuiAddon).handleClick(this, xCenter, yCenter, mouseX, mouseY, mouseButton));
        return false;
    }

    public int getX() {
        return xCenter;
    }

    public int getY() {
        return yCenter;
    }

    @Override
    public T getContainer() {
        return container;
    }

    @Override
    public List<IGuiAddon> getAddons() {
        return addons;
    }

    public IAssetProvider getAssetProvider() {
        return assetProvider;
    }

    public void setAddons(List<IGuiAddon> addons) {
        this.addons = addons;
    }

    public void setAssetProvider(IAssetProvider assetProvider) {
        this.assetProvider = assetProvider;
    }
}
