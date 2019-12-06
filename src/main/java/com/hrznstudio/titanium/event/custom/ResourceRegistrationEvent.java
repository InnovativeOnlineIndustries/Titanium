package com.hrznstudio.titanium.event.custom;

import com.hrznstudio.titanium.material.ResourceMaterial;
import com.hrznstudio.titanium.material.ResourceRegistry;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event used to register materials properly so stuff works properly with async mod loading.
 */
public class ResourceRegistrationEvent extends Event {

    public ResourceMaterial get(String string) {
        return ResourceRegistry.getOrCreate(string);
    }
}
