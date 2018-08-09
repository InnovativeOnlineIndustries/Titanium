/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.util;

import net.minecraft.util.math.MathHelper;

public class StringUtil {
    public static String typingAnimation(String message, int time, int maxTime) {
        float percent = ((float) time / (float) maxTime);
        int messageCount = message.length();
        return message.substring(0, MathHelper.clamp(Math.round(messageCount * percent), 0, messageCount));
    }
}
