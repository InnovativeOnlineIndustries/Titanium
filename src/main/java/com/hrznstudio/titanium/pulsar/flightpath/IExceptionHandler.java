/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.pulsar.flightpath;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * General interface for an exception handler.
 *
 * @author Arkan <arkan@drakon.io>
 */
@ParametersAreNonnullByDefault
public interface IExceptionHandler {

    /**
     * Handle a given exception.
     *
     * @param ex The exception (duh)
     */
    void handle(Exception ex);

    /**
     * Called after all methods have been invoked for cleanup or coalescing.
     */
    void flush();

}
