/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleBase extends Particle implements IParticle {
    private int lifetime = 0;

    public ParticleBase(World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
        super(world, x, y, z, 0, 0, 0);
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;
        if (data.length >= 1) {
            lifetime = (int) data[0];
        }
        this.maxAge = lifetime;
        ResourceLocation texture = ParticleRegistry.particleTextures.get(getClass().getName().toLowerCase());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(texture.toString());
        this.setParticleTexture(sprite);
        this.particleScale = 1.0f;
        this.canCollide = false;
    }


    @Override
    public void tick() {
        super.tick();
        this.motionX *= 0.95f;
        this.motionY *= 0.95f;
        this.motionZ *= 0.95f;
        lifetime--;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public boolean alive() {
        return lifetime > 0;
    }

    @Override
    public boolean isAdditive() {
        return false;
    }

    @Override
    public boolean renderThroughBlocks() {
        return false;
    }
}