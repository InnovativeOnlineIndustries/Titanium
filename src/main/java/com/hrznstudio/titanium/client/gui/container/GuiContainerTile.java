package com.hrznstudio.titanium.client.gui.container;

import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.container.ContainerTileBase;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiContainerTile extends GuiContainerBase<ContainerTileBase> implements IHasContainer<ContainerTileBase> {
    private final ContainerTileBase containerTileBase;

    public GuiContainerTile(Container container, PlayerInventory inventory, ITextComponent title) {
        super((ContainerTileBase) container, inventory, title);
        this.containerTileBase = (ContainerTileBase) container;
    }

    public GuiContainerTile(ContainerTileBase containerTileBase, PlayerInventory inventory, ITextComponent title) {
        super(containerTileBase, inventory, title);
        this.containerTileBase = containerTileBase;
        setAssetProvider(containerTileBase.getTile().getAssetProvider());
        List<IGuiAddon> list = new ArrayList<>();
        containerTileBase.getTile().getGuiAddons().forEach(factory -> list.add(factory.create()));
        setAddons(list);
    }
}
