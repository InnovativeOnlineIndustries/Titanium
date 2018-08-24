/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.core.event;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class SunBrightnessEvent extends WorldEvent {

    private final float partialTicks;
    private float brightness;

    public SunBrightnessEvent(World world, float partialTicks, float brightness) {
        super(world);

        this.partialTicks = partialTicks;
        this.brightness = brightness;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
