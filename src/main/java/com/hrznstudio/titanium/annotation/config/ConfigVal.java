/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.annotation.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ConfigVal {

    String comment() default "";

    String value() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface InRangeLong {

        long max() default Long.MAX_VALUE;

        long min() default Long.MIN_VALUE;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface InRangeInt {

        int max() default Integer.MAX_VALUE;

        int min() default Integer.MIN_VALUE;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface InRangeDouble {

        double max() default Double.MAX_VALUE;

        double min() default Double.MIN_VALUE;

    }
}
