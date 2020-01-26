/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public enum FilenameFilterUtil implements java.io.FilenameFilter {
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

    private String[] extensions;

    FilenameFilterUtil(String... suffix) {
        this.extensions = suffix;
    }

    public static boolean isKnown(File dir, String name) {
        for (FilenameFilterUtil filter : values())
            if (filter.accept(dir, name))
                return true;
        return false;
    }

    public String[] getExtensions() {
        return this.extensions;
    }

    @Override
    public boolean accept(File dir, String name) {
        return FilenameUtils.isExtension(name, extensions);
    }
}