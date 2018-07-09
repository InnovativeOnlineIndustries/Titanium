/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.core;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

import java.util.Map;

public class Mappings {

    /* THIS MAPPING DATABASE HAS BEEN BASED OFF OF 'snapshot_20180406' */

    private static Map<String, String> methodMappings = Maps.newHashMap();
    private static Map<String, String> fieldMappings = Maps.newHashMap();

    static {
        methodMappings.put("func_72971_b", "getSunBrightness");
        methodMappings.put("func_78472_g", "updateLightmap");
        methodMappings.put("func_184389_a", "getModelForState"); // BlockModelShapes
        methodMappings.put("func_174954_c", "getModelForState"); // BlockRendererDispatcher
        methodMappings.put("func_178581_b", "rebuildChunk");

        fieldMappings.put("field_175028_a", "blockModelShapes");

    }

    public static String getMethod(String obf) {
        if (TitaniumLoader.obfuscated)
            return obf;

        if (!methodMappings.containsKey(obf)) {
            throw new IllegalStateException("Cannot find util for method '" + obf + "'!");
        }

        return methodMappings.get(obf);
    }

    public static String getField(String obf) {
        if (TitaniumLoader.obfuscated)
            return obf;

        if (!fieldMappings.containsKey(obf)) {
            throw new IllegalStateException("Cannot find util for method '" + obf + "'!");
        }
        return fieldMappings.get(obf);
    }

    public static String getClass(String deobf) {
        if (!TitaniumLoader.obfuscated)
            return deobf;

        return FMLDeobfuscatingRemapper.INSTANCE.map(deobf);
    }
}
