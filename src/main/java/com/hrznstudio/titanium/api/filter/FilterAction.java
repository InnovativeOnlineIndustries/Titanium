package com.hrznstudio.titanium.api.filter;

import java.util.function.BiPredicate;

public class FilterAction<T extends Object> {

    private final BiPredicate<IFilter<T>, T> filterCheck;

    public FilterAction(BiPredicate<IFilter<T>, T> filterCheck) {
        this.filterCheck = filterCheck;
    }

    public BiPredicate<IFilter<T>, T> getFilterCheck() {
        return filterCheck;
    }

}
