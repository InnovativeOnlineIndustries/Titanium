/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
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
