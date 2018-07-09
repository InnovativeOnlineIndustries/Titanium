/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.core.transform;

import org.objectweb.asm.Opcodes;

public abstract class BaseTransformer implements Opcodes {

    public abstract String[] getClasses();

    public abstract byte[] transform(String name, byte[] data);
}
