/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class ModuleController {
    private final String modid;
    private final AnnotationConfigManager configManager = new AnnotationConfigManager();
    private final PluginManager modPluginManager;
    private final DeferredRegistryHelper deferredRegistryHelper;
    private final List<TitaniumTab> titaniumTabs;

    public ModuleController() {
        this.modid = ModLoadingContext.get().getActiveContainer().getModId();
        this.modPluginManager = new PluginManager(modid, FeaturePlugin.FeaturePluginType.MOD, featurePlugin -> ModList.get().isLoaded(featurePlugin.value()), true);
        this.modPluginManager.execute(PluginPhase.CONSTRUCTION);
        this.deferredRegistryHelper = new DeferredRegistryHelper(this.modid);
        this.titaniumTabs = new ArrayList<>();
        onPreInit();
        onInit();
        onPostInit();
    }

    private void addConfig(AnnotationConfigManager.Type type) {
        for (Class configClass : type.getConfigClass()) {
            if (configManager.isClassManaged(configClass)) return;
        }
        configManager.add(type);
    }

    public RegistryObject<CreativeModeTab> addCreativeTab(String name, Supplier<ItemStack> icon, String title, TitaniumTab tab){
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
        AnnotationUtil.getFilteredAnnotatedClasses(ConfigFile.class, modid).forEach(aClass -> {
            ConfigFile annotation = (ConfigFile) aClass.getAnnotation(ConfigFile.class);
            addConfig(AnnotationConfigManager.Type.of(annotation.type(), aClass).setName(annotation.value()));
        });
        EventManager.mod(ModConfigEvent.Loading.class).process(ev -> {
            configManager.inject(ev.getConfig().getSpec());
            this.modPluginManager.execute(PluginPhase.CONFIG_LOAD);
        }).subscribe();
        EventManager.mod(ModConfigEvent.Reloading.class).process(ev -> {
            configManager.inject(ev.getConfig().getSpec());
            this.modPluginManager.execute(PluginPhase.CONFIG_RELOAD);
        }).subscribe();
        EventManager.mod(GatherDataEvent.class).process(this::addDataProvider).subscribe();
        EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> this.modPluginManager.execute(PluginPhase.CLIENT_SETUP)).subscribe();
        EventManager.mod(FMLCommonSetupEvent.class).process(fmlClientSetupEvent -> this.modPluginManager.execute(PluginPhase.COMMON_SETUP)).subscribe();
        EventManager.mod(BuildCreativeModeTabContentsEvent.class).process(buildCreativeModeTabContentsEvent -> {
            for (TitaniumTab titaniumTab : titaniumTabs) {
                if (titaniumTab.getResourceLocation().equals(buildCreativeModeTabContentsEvent.getTabKey().location())){
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
