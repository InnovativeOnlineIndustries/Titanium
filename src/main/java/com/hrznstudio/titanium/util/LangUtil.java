/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.util.text.TextComponentTranslation;

public class LangUtil {

    public static String get(String string, Object... args) {
        return new TextComponentTranslation(string, args).getFormattedText();
    }
}
