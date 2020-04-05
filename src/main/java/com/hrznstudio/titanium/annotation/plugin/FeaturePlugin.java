package com.hrznstudio.titanium.annotation.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FeaturePlugin {

    String value();

    FeaturePluginType type();

    enum FeaturePluginType {
        MOD,
        FEATURE
    }

}
