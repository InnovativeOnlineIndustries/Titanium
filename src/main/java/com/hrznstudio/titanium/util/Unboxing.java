/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import javax.annotation.Nullable;

public class Unboxing {

    public static long safelyUnbox(@Nullable Long l) {
        if (l == null)
            return 0;
        return l;
    }

    public static int safelyUnbox(@Nullable Integer i) {
        if (i == null)
            return 0;
        return i;
    }

    public static double safelyUnbox(@Nullable Double d) {
        if (d == null)
            return 0;
        return d;
    }

    public static boolean safelyUnbox(@Nullable Boolean b) {
        if (b == null)
            return false;
        return b;
    }

    public static short safelyUnbox(@Nullable Short s) {
        if (s == null)
            return 0;
        return s;
    }

    public static float safelyUnbox(@Nullable Float f) {
        if (f == null)
            return 0;
        return f;
    }
}
