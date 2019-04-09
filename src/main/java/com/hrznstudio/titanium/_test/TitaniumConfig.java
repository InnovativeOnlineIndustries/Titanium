/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.annotation.config.ConfigVal;


public class TitaniumConfig {

    @ConfigVal(comment = "A Boolean that is true by default")
    public static boolean thisIsABoolean = true;
    @ConfigVal(comment = "A Boolean that is false by default", value = "NOT_BOOLEAN")
    public static boolean thisIsNotABoolean = false;
    @ConfigVal
    public static int intAngery = 7;
    @ConfigVal
    public static Dabber dabber = new Dabber();

    public static class Dabber {

        @ConfigVal
        public static String dabby = "lil dab";

    }
}
