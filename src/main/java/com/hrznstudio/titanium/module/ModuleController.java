/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.api.ISpecialCreativeTabItem;
import com.hrznstudio.titanium.config.AnnotationConfigManager;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.plugin.PluginManager;
import com.hrznstudio.titanium.plugin.PluginPhase;
import com.hrznstudio.titanium.tab.TitaniumTab;
import com.hrznstudio.titanium.util.AnnotationUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class ModuleController {
    private final String modid;
    private final ModContainer container;
    private final AnnotationConfigManager configManager = new AnnotationConfigManager();
    private final PluginManager modPluginManager;
    private final DeferredRegistryHelper deferredRegistryHelper;
    private final List<TitaniumTab> titaniumTabs;

    public ModuleController(ModContainer container) {
        this.modid = container.getModId();
        this.container = container;
        this.modPluginManager = new PluginManager(modid, FeaturePlugin.FeaturePluginType.MOD, featurePlugin -> ModList.get().isLoaded(featurePlugin.value()), true);
        this.modPluginManager.execute(PluginPhase.CONSTRUCTION);
        this.deferredRegistryHelper = new DeferredRegistryHelper(this.modid);
        this.titaniumTabs = new ArrayList<>();
        onPreInit();
        onInit();
        onPostInit();
    }

    private void addConfig(ModContainer container, AnnotationConfigManager.Type type) {
        for (Class configClass : type.getConfigClass()) {
            if (configManager.isClassManaged(configClass)) return;
        }
        configManager.add(container, type);
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> addCreativeTab(String name, Supplier<ItemStack> icon, String title, TitaniumTab tab){
        this.titaniumTabs.add(tab);
        return this.getRegistries().registerGeneric(Registries.CREATIVE_MODE_TAB, name, () -> CreativeModeTab.builder()
            .icon(icon)
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup." + title))
            .build());
    }

    public void onPreInit() {
        this.modPluginManager.execute(PluginPhase.PRE_INIT);
    }

    public void onInit() {
        initModules();

        this.modPluginManager.execute(PluginPhase.INIT);
    }

    public void onPostInit() {
        EventManager.mod(ModConfigEvent.Loading.class).process(ev -> {
            configManager.inject(ev.getConfig().getSpec());
            this.modPluginManager.execute(PluginPhase.CONFIG_LOAD);
        }).subscribe();
        EventManager.mod(ModConfigEvent.Reloading.class).process(ev -> {
            configManager.inject(ev.getConfig().getSpec());
            this.modPluginManager.execute(PluginPhase.CONFIG_RELOAD);
        }).subscribe();
        AnnotationUtil.getFilteredAnnotatedClasses(ConfigFile.class, modid).forEach(aClass -> {
            ConfigFile annotation = (ConfigFile) aClass.getAnnotation(ConfigFile.class);
            addConfig(container, AnnotationConfigManager.Type.of(annotation.type(), aClass).setName(annotation.value()));
        });
        EventManager.mod(GatherDataEvent.Client.class).process(this::addDataProvider).subscribe();
        EventManager.mod(GatherDataEvent.Server.class).process(this::addDataProvider).subscribe();
        EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> this.modPluginManager.execute(PluginPhase.CLIENT_SETUP)).subscribe();
        EventManager.mod(FMLCommonSetupEvent.class).process(fmlClientSetupEvent -> this.modPluginManager.execute(PluginPhase.COMMON_SETUP)).subscribe();
        EventManager.mod(BuildCreativeModeTabContentsEvent.class).process(buildCreativeModeTabContentsEvent -> {
            for (TitaniumTab titaniumTab : titaniumTabs) {
                if (titaniumTab.getIdentifier().equals(buildCreativeModeTabContentsEvent.getTabKey().identifier())) {
                    for (Item item : titaniumTab.getTabList()) {
                        if (item instanceof ISpecialCreativeTabItem specialCreativeTabItem){
                            specialCreativeTabItem.addToTab(buildCreativeModeTabContentsEvent);
                        } else {
                            buildCreativeModeTabContentsEvent.accept(item);
                        }
                    }
                }
            }
        }).subscribe();
        this.modPluginManager.execute(PluginPhase.POST_INIT);
    }

    protected abstract void initModules();

    public void addDataProvider(GatherDataEvent event) {

    }

    public DeferredRegistryHelper getRegistries() {
        return deferredRegistryHelper;
    }
}
