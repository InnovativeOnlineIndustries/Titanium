package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.annotation.config.ConfigVal;

public class TitaniumConfig {

    @ConfigVal(comment = "A Boolean that is true by default")
    public static boolean thisIsABoolean = true;
    @ConfigVal(comment = "A Boolean that is false by default", value = "NOT_BOOLEAN")
    public static boolean thisIsNotABoolean = false;
    @ConfigVal
    public static int intAngery = 7;
}
