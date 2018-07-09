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

public class TransformBlockRendererDispatcher extends BaseTransformer {

    @Override
    public String[] getClasses() {
        return new String[]{"net.minecraft.client.renderer.BlockRendererDispatcher"};
    }

    @Override
    public byte[] transform(String name, byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        TitaniumTransformer.info("Found BlockRendererDispatcher.class, starting to patch");

        MethodNode getModelForState = null;
        for (MethodNode node : classNode.methods) {
            if (node.name.equals(Mappings.getMethod("func_174954_c"))) {
                getModelForState = node;
                break;
            }
        }

        if (getModelForState != null) {
            TitaniumTransformer.info("Found getModelForState method. Inserting event dispatch");

            InsnList list = new InsnList();

            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(
                    GETFIELD,
                    Mappings.getClass("net/minecraft/client/renderer/BlockRendererDispatcher"),
                    Mappings.getField("field_175028_a"),
                    "L" + Mappings.getClass("net/minecraft/client/renderer/BlockModelShapes" + ";")
            ));
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(
                    GETFIELD,
                    Mappings.getClass("net/minecraft/client/renderer/BlockRendererDispatcher"),
                    Mappings.getField("field_175028_a"),
                    "L" + Mappings.getClass("net/minecraft/client/renderer/BlockModelShapes" + ";")
            ));
            list.add(new VarInsnNode(ALOAD, 1));

            list.add(new MethodInsnNode(
                    INVOKEVIRTUAL,
                    Mappings.getClass("net/minecraft/client/renderer/BlockModelShapes"),
                    Mappings.getMethod("func_184389_a"),
                    ASMUtils.buildDescription(
                            Mappings.getClass("net/minecraft/block/state/IBlockState"),
                            Mappings.getClass("net/minecraft/client/renderer/block/model/IBakedModel")
                    ),
                    false
            ));

            list.add(new MethodInsnNode(
                    INVOKESTATIC,
                    "com/hrznstudio/edx/asm/event/EventDispatcher",
                    "getBlockModel",
                    ASMUtils.buildDescription(
                            Mappings.getClass("net/minecraft/client/renderer/BlockModelShapes"),
                            Mappings.getClass("net/minecraft/block/state/IBlockState"),
                            Mappings.getClass("net/minecraft/client/renderer/block/model/IBakedModel"),
                            Mappings.getClass("net/minecraft/client/renderer/block/model/IBakedModel")
                    ),
                    false
            ));

            list.add(new InsnNode(ARETURN));

            getModelForState.instructions.clear();
            getModelForState.instructions.insert(list);
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);

        return writer.toByteArray();
    }
}
