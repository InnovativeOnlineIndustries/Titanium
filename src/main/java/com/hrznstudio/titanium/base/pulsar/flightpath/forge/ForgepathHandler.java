/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.pulsar.flightpath.forge;

import java.lang.annotation.*;

/**
 * Marker interface for classes that Flightpath should construct for event handling in Forge-based mods.
 *
 * @author Arkan <arkan@drakon.io>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForgepathHandler {
}
