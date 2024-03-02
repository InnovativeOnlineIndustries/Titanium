/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.referenceholder;

import net.minecraft.world.inventory.DataSlot;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class FunctionReferenceHolder extends DataSlot {
    private final IntConsumer setter;
    private final IntSupplier getter;

    public FunctionReferenceHolder(IntConsumer setter, IntSupplier getter) {
        this.setter = setter;
        this.getter = getter;
    }

    @Override
    public int get() {
        return getter.getAsInt();
    }

    @Override
    public void set(int i) {
        setter.accept(i);
    }
}
