package com.hrznstudio.titanium.client.gui.container;

import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.container.impl.ContainerTileBase;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiContainerTileBase extends GuiContainerBase<ContainerTileBase> implements IHasContainer<ContainerTileBase> {
    private final ContainerTileBase containerTileBase;

    public GuiContainerTileBase(ContainerTileBase containerTileBase, PlayerInventory inventory, ITextComponent title) {
        super(containerTileBase, inventory, title);
        this.containerTileBase = containerTileBase;
        setAssetProvider(containerTileBase.getTile().getAssetProvider());
        List<IGuiAddon> list = new ArrayList<>();
        containerTileBase.getTile().getGuiAddons().forEach(factory -> list.add(factory.create()));
        setAddons(list);
    }
}
