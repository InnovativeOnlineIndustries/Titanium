/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

public final class SidedHandlerUtil {
    private SidedHandlerUtil() {
    }

    public static Dist getSide() {
        return FMLEnvironment.dist;
    }

    public static void runOn(Dist side, Supplier<Runnable> toRun) {
        if (is(side)) {
            toRun.get().run();
        }
    }

    public static boolean is(Dist side) {
        return side == getSide();
    }

    public static <T> T getSided(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        if (is(Dist.CLIENT)) {
            return clientTarget.get().get();
        } else {
            return serverTarget.get().get();
        }
    }
}