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
