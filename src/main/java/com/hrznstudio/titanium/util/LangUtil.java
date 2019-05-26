package com.hrznstudio.titanium.util;

import net.minecraft.util.text.TextComponentTranslation;

public class LangUtil {

    public static String get(String string, Object... args) {
        return new TextComponentTranslation(string, args).getFormattedText();
    }
}
