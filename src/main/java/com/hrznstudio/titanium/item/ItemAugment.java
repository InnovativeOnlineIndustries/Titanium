/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
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
