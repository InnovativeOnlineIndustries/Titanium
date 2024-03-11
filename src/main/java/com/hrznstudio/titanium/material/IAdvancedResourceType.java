/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.api.material.IResourceType;

public interface IAdvancedResourceType {

    int getColor(ResourceMaterial material, int tintIndex);

    JsonObject generate(IResourceType type);

}
