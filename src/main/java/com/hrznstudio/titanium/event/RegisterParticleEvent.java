/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.event;

import com.hrznstudio.titanium.particle.ParticleBase;
import com.hrznstudio.titanium.particle.ParticleRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class RegisterParticleEvent extends Event {

    public RegisterParticleEvent() {
        super();
    }

    public void registerParticle(Class<? extends ParticleBase> particleClass, ResourceLocation texture) {
        ParticleRegistry.registerParticle(particleClass, texture);
    }
}
