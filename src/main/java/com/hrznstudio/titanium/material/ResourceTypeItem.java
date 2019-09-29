package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.item.ItemBase;

public class ResourceTypeItem extends ItemBase {

    private final ResourceMaterial material;
    private final ResourceType type;

    public ResourceTypeItem(ResourceMaterial material, ResourceType type) {
        super(material.getMaterialType() + "_" + type.getName(), new Properties());
        this.material = material;
        this.type = type;
    }

    public ResourceMaterial getMaterial() {
        return material;
    }

    public ResourceType getType() {
        return type;
    }

}
