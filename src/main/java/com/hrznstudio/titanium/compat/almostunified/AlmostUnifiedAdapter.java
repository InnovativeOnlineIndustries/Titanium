package com.hrznstudio.titanium.compat.almostunified;

import com.almostreliable.unified.api.AlmostUnifiedLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;

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
            return AlmostUnifiedLookup.INSTANCE.getPreferredItemForTag(tag);
        }
    }
}
