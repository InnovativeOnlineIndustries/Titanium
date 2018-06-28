/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.util;

import java.io.File;

public enum FilenameFilter implements java.io.FilenameFilter {
    XML("xml"),
    JSON("json"),
    CFG("cfg"),
    CONF("conf"),
    ZENSCRIPT("zs"),
    ZIP("zip"),
    TXT("txt"),
    PNG("png"),
    JAR("jar"),
    EXECUTABLE("exe"),
    GITIGNORE("gitignore"),
    JAVA("java"),
    GZIP("gzip"),
    LOG("log"),
    DAT("dat"),
    OBJ("obj"),
    CLASS("class");

    private String suffix;

    FilenameFilter(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(String.format(".%s", getSuffix().toLowerCase()));
    }
}