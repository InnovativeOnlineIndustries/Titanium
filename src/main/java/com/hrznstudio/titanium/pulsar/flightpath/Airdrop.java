/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.pulsar.flightpath;

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
