/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;

@ConfigFile
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
