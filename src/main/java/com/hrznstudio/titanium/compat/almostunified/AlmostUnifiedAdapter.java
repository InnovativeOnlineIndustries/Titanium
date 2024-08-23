/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.compat.almostunified;

import com.almostreliable.unified.api.AlmostUnified;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import javax.annotation.Nullable;

public class AlmostUnifiedAdapter {

    public static boolean isLoaded() {
        return ModList.get().isLoaded("almostunified");
    }

    @Nullable
    public static Item getPreferredItemForTag(TagKey<Item> tagKey) {
        if (isLoaded()) {
            return Adapter.getPreferredItemForTag(tagKey);
        }

        return null;
    }

    private static class Adapter {
        private static Item getPreferredItemForTag(TagKey<Item> tag) {
             return AlmostUnified.INSTANCE.getTagTargetItem(tag);
        }
    }
}
