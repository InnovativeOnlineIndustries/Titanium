/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.util;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.function.Supplier;

public final class SidedHandler {
    private SidedHandler() {
    }

    public static Side getSide() {
        return FMLCommonHandler.instance().getSide();
    }

    public static void runOn(Side side, Supplier<Runnable> toRun) {
        if (is(side)) {
            toRun.get().run();
        }
    }

    public static boolean is(Side side) {
        return side == getSide();
    }

    public static <T> T getSided(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        if (is(Side.CLIENT)) {
            return clientTarget.get().get();
        } else {
            return serverTarget.get().get();
        }
    }
}