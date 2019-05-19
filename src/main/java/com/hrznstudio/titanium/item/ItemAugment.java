/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.augment.IAugment;
import com.hrznstudio.titanium.api.augment.IAugmentType;

public abstract class ItemAugment extends ItemBase implements IAugment {
    private IAugmentType type;

    public ItemAugment(String name, Properties properties, IAugmentType type) {
        super(name, properties);
        this.type = type;
    }

    @Override
    public IAugmentType getAugmentType() {
        return type;
    }
}
