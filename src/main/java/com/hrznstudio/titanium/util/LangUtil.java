/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LangUtil {

    public static MutableComponent get(String string, Object... args) {
        return Component.translatable(string, args);
    }

    public static String getString(String string, Object... args) {
        return Component.translatable(string, args).getString();
    }

}
