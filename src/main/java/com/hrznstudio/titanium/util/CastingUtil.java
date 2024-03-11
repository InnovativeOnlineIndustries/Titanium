/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import java.util.Optional;
import java.util.function.Function;

public class CastingUtil {
    public static <T, U> Function<T, Optional<U>> attemptCast(Class<U> clazz) {
        return input -> {
            if (clazz.isInstance(input)) {
                return Optional.of(clazz.cast(input));
            } else {
                return Optional.empty();
            }
        };
    }
}
