/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.core.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateLightmapEvent extends Event {

    private final int arrayPosition;

    private int red;
    private int green;
    private int blue;

    public UpdateLightmapEvent(int arrayPosition, int red, int green, int blue) {
        this.arrayPosition = arrayPosition;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getArrayPosition() {
        return arrayPosition;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
