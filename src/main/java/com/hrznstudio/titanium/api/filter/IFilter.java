package com.hrznstudio.titanium.api.filter;

import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Predicate;

public interface IFilter<T extends Object> extends INBTSerializable<CompoundNBT>, IGuiAddonProvider {

    String getName();

    boolean acceptsAsFilter(ItemStack filter);

    void setFilter(int slot, ItemStack stack);

    void setFilter(int slot, FilterSlot<T> filterSlot);

    FilterSlot<T>[] getFilter();

    Type getType();

    FilterAction<T> getAction();

    void toggleFilterMode();

    void selectNextFilter();

    default boolean matches(T object) {
        return getType().getFilter().test(getAction().getFilterCheck().test(this, object));
    }

    public enum Type {
        WHITELIST(filter -> filter),
        BLACKLIST(filter -> !filter);

        private final Predicate<Boolean> filter;

        Type(Predicate<Boolean> filter) {
            this.filter = filter;
        }

        public Predicate<Boolean> getFilter() {
            return filter;
        }
    }

}
