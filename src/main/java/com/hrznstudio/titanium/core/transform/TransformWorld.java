/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.core.transform;

import com.hrznstudio.titanium.core.ASMUtils;
import com.hrznstudio.titanium.core.Mappings;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class TransformWorld extends BaseTransformer {

    @Override
    public String[] getClasses() {
        return new String[]{"net.minecraft.world.World"};
    }

    @Override
    public byte[] transform(String name, byte[] data) {
        ClassReader classReader = new ClassReader(data);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        TitaniumTransformer.info("Found World.class, starting to patch");

        MethodNode target = null;
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(Mappings.getMethod("func_72971_b"))) {
                target = methodNode;
            }
        }

        if (target != null) {
            TitaniumTransformer.info("Found getSunBrightness method. Inserting event dispatch");

            AbstractInsnNode insertionTargetAload = null;
            AbstractInsnNode insertionTargetMethod = null;
            Iterator<AbstractInsnNode> iterator = target.instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode node = iterator.next();
                if (insertionTargetAload == null && node.getOpcode() == ALOAD && ((VarInsnNode) node).var == 0) {
                    insertionTargetAload = node;
                }

                if (insertionTargetMethod == null && node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).desc.equals("(F)F")) {
                    insertionTargetMethod = node;
                }
            }

            if (insertionTargetAload != null) {
                target.instructions.insert(insertionTargetAload, new VarInsnNode(ALOAD, 0));
                target.instructions.insert(insertionTargetAload, new VarInsnNode(FLOAD, 1));
            }

            if (insertionTargetMethod != null) {
                InsnList list = new InsnList();
                list.add(new MethodInsnNode(
                        INVOKESTATIC,
                        "com/hrznstudio/edx/asm/event/EventDispatcher",
                        "getSunBrightness",
                        ASMUtils.buildDescription(Mappings.getClass("net/minecraft/world/World"), "F", "F", "F"),
                        false
                ));
                target.instructions.insert(insertionTargetMethod, list);
            }
        }

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
