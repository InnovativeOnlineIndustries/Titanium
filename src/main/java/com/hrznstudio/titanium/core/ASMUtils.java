/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.core;

public class ASMUtils {

    public static String buildDescription(String... args) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (int i = 0; i < args.length - 1; i++) {
            builder.append(formatClassName(args[i]));
        }
        builder.append(')');
        builder.append(formatClassName(args[args.length - 1]));
        return builder.toString();
    }

    private static String formatClassName(String arg) {
        if (arg.length() == 1)
            return arg;

        arg = arg.replace(".", "/");
        arg = 'L' + arg + ';';
        return arg;
    }
}
