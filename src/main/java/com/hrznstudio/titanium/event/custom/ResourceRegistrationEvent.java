/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

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
