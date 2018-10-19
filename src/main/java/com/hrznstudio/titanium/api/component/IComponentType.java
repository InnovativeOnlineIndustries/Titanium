package com.hrznstudio.titanium.api.component;

import net.minecraft.util.IStringSerializable;

public interface IComponentType<T extends IComponent> extends IStringSerializable {
    boolean isInstance(IComponent component);
    T cast(IComponent component);
}
