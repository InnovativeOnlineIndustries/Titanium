package com.hrznstudio.titanium.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IIngredient {

    String getKey();

    public static class TagIngredient implements IIngredient {

        public final String tag;

        private TagIngredient(String tag) {
            this.tag = tag;
        }

        public static TagIngredient of(String string) {
            return new TagIngredient(string);
        }

        @Override
        public String getKey() {
            return tag;
        }
    }

    public static class ItemStackIngredient implements IIngredient {

        public final String item;

        private ItemStackIngredient(String item) {
            this.item = item;
        }

        public static ItemStackIngredient of(ItemStack stack) {
            return new ItemStackIngredient(stack.getItem().getRegistryName().toString());
        }

        @Override
        public String getKey() {
            return new ResourceLocation(item).getPath();
        }
    }
}
