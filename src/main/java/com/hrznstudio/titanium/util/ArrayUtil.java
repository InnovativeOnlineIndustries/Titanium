/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import java.util.function.Consumer;

public class ArrayUtil {
    @SafeVarargs
    public static <T> void forEach(Consumer<T> consumer, T... values) {
        for (T t : values)
            consumer.accept(t);
    }
}
