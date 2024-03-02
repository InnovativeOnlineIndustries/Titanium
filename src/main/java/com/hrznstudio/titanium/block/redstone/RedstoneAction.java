/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.redstone;

import com.hrznstudio.titanium.api.IEnumValues;
import com.hrznstudio.titanium.api.redstone.IRedstoneAction;
import com.hrznstudio.titanium.api.redstone.IRedstoneState;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum RedstoneAction implements IRedstoneAction, IEnumValues<RedstoneAction> {

    IGNORE(state -> true, false),
    NO_REDSTONE(state -> !state.isReceivingRedstone(), false),
    WITH_REDSTONE(IRedstoneState::isReceivingRedstone, false),
    ONCE(state -> true, true);

    private final Predicate<IRedstoneState> canRun;
    private final boolean startsOnChange;

    RedstoneAction(Predicate<IRedstoneState> canRun, boolean startsOnChange) {
        this.canRun = canRun;
        this.startsOnChange = startsOnChange;
    }

    @Override
    public boolean canRun(IRedstoneState state) {
        return canRun.test(state);
    }

    @Override
    public boolean startsOnChange() {
        return startsOnChange;
    }

    @Override
    public List<RedstoneAction> getValues() {
        return Arrays.asList(values());
    }

    @Override
    public RedstoneAction getValue(String name) {
        return this.getValues().stream().filter(t -> t.getName().equals(name)).findFirst().orElse(IGNORE);
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }
}
