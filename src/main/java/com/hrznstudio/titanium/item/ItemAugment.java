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
