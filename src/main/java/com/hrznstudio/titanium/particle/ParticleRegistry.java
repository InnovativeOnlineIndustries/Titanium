/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.particle;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ParticleRegistry {

    public static Map<String, ResourceLocation> particleTextures = new HashMap<>();

    private static Map<String, Constructor<? extends ParticleBase>> particles = new HashMap<>();

    public static String registerParticle(Class<? extends ParticleBase> particleClass, ResourceLocation texture) {
        String name = particleClass.getName().toLowerCase();
        try {
            if (particles.containsKey(name) || particleTextures.containsKey(name)) {
                System.out.println("WARNING: PARTICLE ALREADY REGISTERED WITH NAME \"" + name + "\"!");
            } else {
                particles.put(name,
                        particleClass.getConstructor(World.class, double.class, double.class, double.class, double.class, double.class, double.class, double[].class));
                particleTextures.put(name, texture);
            }
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static Map<String, Constructor<? extends ParticleBase>> getParticles() {
        return particles;
    }
}
