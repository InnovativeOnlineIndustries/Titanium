/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.proton.control;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.ImmutableList;
import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.config.AnnotationConfigManager;
import com.hrznstudio.titanium.proton.EventReceiver;
import com.hrznstudio.titanium.proton.Proton;
import com.hrznstudio.titanium.proton.ProtonData;
import com.hrznstudio.titanium.proton.api.IAlternativeEntries;
import com.hrznstudio.titanium.proton.api.IColorProvider;
import com.hrznstudio.titanium.proton.api.IItemColorProvider;
import com.hrznstudio.titanium.proton.api.RegistryManager;
import com.hrznstudio.titanium.util.AnnotationUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

public abstract class ProtonManager implements RegistryManager {
    private final String modid;
    private final Map<Class<? extends IForgeRegistryEntry>, List<?>> entries = new HashMap<>();
    private final AnnotationConfigManager configManager = new AnnotationConfigManager();
    private final List<Proton> protons = new ArrayList<>();

    public ProtonManager() {
        modid = ModLoadingContext.get().getActiveContainer().getModId();
        MinecraftForge.EVENT_BUS.register(this);
        List<Method> methods = new ArrayList<>(getMethods());
        AnnotationUtil.getFilteredAnnotatedClasses(ProtonData.class, modid).forEach(aClass -> {
            addProton(protonManager -> {
                try {
                    return (Proton) aClass.getConstructor(ProtonManager.class).newInstance(protonManager);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        CommentedFileConfig config = CommentedFileConfig.of("config/" + modid + "/modules.toml");
        config.load();
        for (Proton proton : protons) {
            if (!proton.getData().forced()) {
                String module = "modules." + proton.getData().value();
                String desc = proton.getData().description();
                if (!desc.isEmpty())
                    config.setComment(module, desc);
                config.add(module, proton.getData().defaultActive());
                proton.setActive(config.getOrElse(module, proton.getData().defaultActive()));
            } else {
                proton.setActive(true);
            }
            if (proton.isActive()) {
                methods.addAll(proton.getEventMethods());
                proton.init();
            }
        }
        config.save();
        config.close();
        methods.forEach(method -> {
            EventReceiver eventReceiver = method.getAnnotation(EventReceiver.class);
            EventPriority priority = eventReceiver.priority();
            FMLJavaModLoadingContext.get().getModEventBus().addListener(priority, event -> {
                try {
                    Class<?> param = method.getParameterTypes()[0];
                    if (event.getClass().isAssignableFrom(param)) {
                        if (GenericEvent.class.isAssignableFrom(param)) {
                            if (event instanceof GenericEvent) {
                                Type[] arr = method.getGenericParameterTypes();
                                Type type;
                                ParameterizedType parameterizedType = (ParameterizedType) arr[0];
                                Type actual = parameterizedType.getActualTypeArguments()[0];
                                if (actual instanceof ParameterizedType) {
                                    type = ((ParameterizedType) actual).getRawType();
                                } else {
                                    type = actual;
                                }
                                if (type == ((GenericEvent) event).getGenericType()) {
                                    method.invoke(this, event);
                                }
                            }
                        } else {
                            method.invoke(this, event);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });
        FMLJavaModLoadingContext.get().getModEventBus().addListener(event -> {
            if (event instanceof RegistryEvent.Register) {
                getEntries((Class<IForgeRegistryEntry>) ((RegistryEvent) event).getGenericType()).forEach(t -> ((RegistryEvent.Register) event).getRegistry().register((IForgeRegistryEntry) t));
            }
        });
        AnnotationUtil.getFilteredAnnotatedClasses(ConfigFile.class, modid).forEach(aClass -> {
            ConfigFile annotation = (ConfigFile) aClass.getAnnotation(ConfigFile.class);
            addConfig(AnnotationConfigManager.Type.of(annotation.type(), aClass).setName(annotation.value()));
        });
        AnnotationUtil.getFilteredAnnotatedClasses(ProtonData.class, modid).forEach(aClass -> {
            addProton(protonManager -> {
                try {
                    return (Proton) aClass.getConstructor(ProtonManager.class).newInstance(protonManager);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    private List<Method> getMethods() {
        ImmutableList.Builder<Method> builder = new ImmutableList.Builder<>();
        Class clazz = getClass();
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventReceiver.class)) {
                    if (method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        builder.add(method);
                    }
                }
            }
            clazz = clazz.getSuperclass();
            if (clazz == Object.class)
                clazz = null;
        }
        return builder.build();
    }

    private void addProton(Function<ProtonManager, Proton> protonFunction) {
        protons.add(protonFunction.apply(this));
    }

    public final <T extends IForgeRegistryEntry<T>> List<T> getEntries(Class<T> tClass) {
        if (!entries.containsKey(tClass)) {
            entries.put(tClass, new ArrayList<T>());
        }
        List<T> list = (List<T>) entries.get(tClass);
        return list == null ? Collections.emptyList() : list;
    }

    @EventReceiver
    @OnlyIn(Dist.CLIENT)
    public final void clientSetupTitanium(FMLClientSetupEvent event) {
        getEntries(Item.class).stream()
                .filter(IItemColorProvider.class::isInstance)
                .forEach(i -> Minecraft.getInstance().getItemColors().register(((IItemColorProvider) i)::getColor, i));
        getEntries(Block.class).stream()
                .filter(IColorProvider.class::isInstance)
                .forEach(b -> {
                    Minecraft.getInstance().getItemColors().register(((IColorProvider) b)::getColor, b.asItem());
                    Minecraft.getInstance().getBlockColors().register(((IColorProvider) b)::getColor, b);
                });
    }

    public final String getModid() {
        return modid;
    }

    @EventReceiver
    public final void onConfig(ModConfig.Loading event) {
        configManager.inject();
    }

    @EventReceiver
    public final void onConfigReload(ModConfig.ConfigReloading event) {
        configManager.inject();
    }

    @Override
    public final <T extends IForgeRegistryEntry<T>> void addEntry(Class<T> tClass, T t) {
        getEntries(tClass).add(t);
        if (t instanceof IAlternativeEntries)
            ((IAlternativeEntries) t).addAlternatives(this);
    }

    @Override
    public final <T extends IForgeRegistryEntry<T>> void addEntries(Class<T> tClass, T... ts) {
        for (T t : ts)
            addEntry(tClass, t);
    }

    private void addConfig(AnnotationConfigManager.Type type) {
        for (Class configClass : type.getConfigClass()) {
            if (configManager.isClassManaged(configClass)) return;
        }
        configManager.add(type);
    }
}