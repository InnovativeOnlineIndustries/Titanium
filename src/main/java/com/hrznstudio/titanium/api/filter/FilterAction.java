/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.filter;

import java.util.function.BiPredicate;

public class FilterAction<T> {

    private final BiPredicate<IFilter<T>, T> filterCheck;

    public FilterAction(BiPredicate<IFilter<T>, T> filterCheck) {
        this.filterCheck = filterCheck;
    }

    public BiPredicate<IFilter<T>, T> getFilterCheck() {
        return filterCheck;
    }

}
