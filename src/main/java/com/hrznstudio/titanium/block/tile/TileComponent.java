package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.api.component.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TileComponent extends TileBase implements IComponentTile {
    private final Map<IComponentType, IComponent> componentMap = new HashMap<>();

    public void addComponent(IComponent component) {
        componentMap.put(component.getType(), component);
    }

    @Override
    public <T extends IComponent> T getComponent(IComponentType<T> componentType) {
        return componentType.cast(componentMap.get(componentType));
    }

    @Override
    public void update() {
        super.update();
        for (IComponent component : componentMap.values()) {
            if (component instanceof ITickableComponent)
                ((ITickableComponent) component).update();
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        for (IComponent capabilityComponent : componentMap.values())
            if (capabilityComponent instanceof ICapabilityComponent)
                if (((ICapabilityComponent) capabilityComponent).hasCapability(capability, facing))
                    return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        for (IComponent capabilityComponent : componentMap.values())
            if (((ICapabilityComponent) capabilityComponent).hasCapability(capability, facing))
                return ((ICapabilityComponent) capabilityComponent).getCapability(capability, facing);
        return super.getCapability(capability, facing);
    }
}