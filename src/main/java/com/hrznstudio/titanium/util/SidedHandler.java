/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

public final class SidedHandler {
    private SidedHandler() {
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