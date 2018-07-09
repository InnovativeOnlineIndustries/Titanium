/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.core.transform;

import com.google.common.collect.Lists;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TitaniumTransformer implements IClassTransformer {

    public static final Logger LOGGER = LogManager.getLogger("Titanium - ASM");
    private static List<BaseTransformer> transformers = Lists.newArrayList();

    static {
        transformers.add(new TransformWorld());
        transformers.add(new TransformEntityRenderer());
        transformers.add(new TransformBlockRendererDispatcher());
    }

    public static void info(String msg) {
        LOGGER.info(msg);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        for (BaseTransformer transformer : transformers) {
            for (String clazz : transformer.getClasses()) {
                if (transformedName.equals(clazz)) {
                    return transformer.transform(transformedName, data);
                }
            }
        }
        return data;
    }
}
