package com.hrznstudio.titanium.components;

import com.hrznstudio.titanium.api.component.IComponentType;
import com.hrznstudio.titanium.api.component.IDataComponent;

public class ComponentTypes {
    public static final IComponentType<IDataComponent> DATA_COMPONENT_TYPE = new GenericComponentType<>("data_component", IDataComponent.class);
}