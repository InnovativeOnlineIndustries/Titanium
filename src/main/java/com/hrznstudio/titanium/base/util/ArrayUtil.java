/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.util;

import java.util.function.Consumer;

public class ArrayUtil {
    @SafeVarargs
    public static <T> void forEach(Consumer<T> consumer, T... values) {
        for (T t : values)
            consumer.accept(t);
    }
}
