package com.hrznstudio.titanium.block.redstone;

import com.hrznstudio.titanium.api.IEnumValues;
import com.hrznstudio.titanium.api.redstone.IRedstoneAction;
import com.hrznstudio.titanium.api.redstone.IRedstoneState;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public enum RedstoneAction implements IRedstoneAction, IEnumValues<RedstoneAction> {

    IGNORE(state -> true, false),
    NO_REDSTONE(state -> !state.isReceivingRedstone(), false),
    WITH_REDSTONE(IRedstoneState::isReceivingRedstone, false),
    ONCE(state -> true, true);

    private final Function<IRedstoneState, Boolean> canRun;
    private final boolean startsOnChange;

    RedstoneAction(Function<IRedstoneState, Boolean> canRun, boolean startsOnChange) {
        this.canRun = canRun;
        this.startsOnChange = startsOnChange;
    }

    @Override
    public boolean canRun(IRedstoneState state) {
        return canRun.apply(state);
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
        return this.getValues().stream().filter(t -> t.func_176610_l().equals(name)).findFirst().orElse(IGNORE);
    }

    @Override
    public String func_176610_l() {
        return this.name().toLowerCase();
    }
}
