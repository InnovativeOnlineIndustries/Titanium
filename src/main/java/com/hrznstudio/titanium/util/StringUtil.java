/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.util.math.MathHelper;

public class StringUtil {
    public static String typingAnimation(String message, int time, int maxTime) {
        float percent = ((float) time / (float) maxTime);
        int messageCount = message.length();
        return message.substring(0, MathHelper.clamp(Math.round(messageCount * percent), 0, messageCount));
    }
}
