/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.annotation.config;


import net.minecraftforge.fml.config.ModConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ConfigFile {
    /**
     * The type of the config
     *
     * @return the type
     */
    ModConfig.Type type() default ModConfig.Type.COMMON;

    /**
     * The file name of the config
     *
     * @return the config name
     */
    String value() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Child {

        /**
         * The class which the config file is child to
         *
         * @return the parent class
         */
        Class value();

    }
}
