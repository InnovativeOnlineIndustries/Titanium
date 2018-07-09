/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.core.transform;

import com.hrznstudio.titanium.core.Mappings;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class TransformEntityRenderer extends BaseTransformer {

    @Override
    public String[] getClasses() {
        return new String[]{"net.minecraft.client.renderer.EntityRenderer"};
    }


    @Override
    public byte[] transform(String name, byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        TitaniumTransformer.info("Found EntityRenderer.class, starting to patch");

        MethodNode updateLightmap = null;
        for (MethodNode node : classNode.methods) {
            if (node.name.equals(Mappings.getMethod("func_78472_g"))) {
                updateLightmap = node;
                break;
            }
        }

        if (updateLightmap != null) {
            TitaniumTransformer.info("Found updateLightmap method. Inserting method calls");

            boolean insertedHook = false;

            Iterator<AbstractInsnNode> iterator = updateLightmap.instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode node = iterator.next();

                if (node instanceof VarInsnNode && !insertedHook) {
                    if (node.getOpcode() == ISTORE && ((VarInsnNode) node).var == 23) {
                        InsnList list = new InsnList();

                        list.add(new VarInsnNode(ILOAD, 5));
                        list.add(new VarInsnNode(ILOAD, 21));
                        list.add(new VarInsnNode(ILOAD, 22));
                        list.add(new VarInsnNode(ILOAD, 23));

                        list.add(new MethodInsnNode(
                                INVOKESTATIC,
                                "com/hrznstudio/titanium/core/event/EventDispatcher",
                                "updateLightmap",
                                "(IIII)Lcom/hrznstudio/edx/asm/event/UpdateLightmapEvent;",
                                false
                        ));

                        list.add(new VarInsnNode(ASTORE, 24));

                        // LOAD RED
                        list.add(new VarInsnNode(ALOAD, 24));
                        list.add(new MethodInsnNode(
                                INVOKEVIRTUAL,
                                "com/hrznstudio/edx/asm/event/UpdateLightmapEvent",
                                "getRed",
                                "()I",
                                false
                        ));
                        list.add(new VarInsnNode(ISTORE, 21));

                        // LOAD GREEN
                        list.add(new VarInsnNode(ALOAD, 24));
                        list.add(new MethodInsnNode(
                                INVOKEVIRTUAL,
                                "com/hrznstudio/edx/asm/event/UpdateLightmapEvent",
                                "getGreen",
                                "()I",
                                false
                        ));
                        list.add(new VarInsnNode(ISTORE, 22));

                        // LOAD BLUE
                        list.add(new VarInsnNode(ALOAD, 24));
                        list.add(new MethodInsnNode(
                                INVOKEVIRTUAL,
                                "com/hrznstudio/edx/asm/event/UpdateLightmapEvent",
                                "getBlue",
                                "()I",
                                false
                        ));
                        list.add(new VarInsnNode(ISTORE, 23));

                        updateLightmap.instructions.insert(node.getNext(), list);
                        insertedHook = true;
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);


        byte[] bytes = writer.toByteArray();
        saveBytecode(name, bytes);
        return bytes;
    }

    private void saveBytecode(String name, byte[] bytes) {
        File debugDir = new File("edx/debug/");
        if (!debugDir.exists()) {
            debugDir.mkdirs();
        }
        File outputFile = new File(debugDir, name + ".class");
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            IOUtils.write(bytes, out);
        } catch (IOException e) {
        }
    }
}
