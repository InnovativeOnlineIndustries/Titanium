/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.pulsar.flightpath;

import java.lang.annotation.*;

/**
 * Default annotation for Flightpath subscriptions.
 * <p>
 * We don't use @Subscriber, as too many frameworks use it (which may lead to import hell)
 *
 * @author Arkan <arkan@drakon.io>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Airdrop {
}
