/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
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
