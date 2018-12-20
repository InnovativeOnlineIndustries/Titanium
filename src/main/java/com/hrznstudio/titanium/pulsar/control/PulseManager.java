/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.pulsar.control;

import com.google.common.eventbus.Subscribe;
import com.hrznstudio.titanium.pulsar.config.ForgeConfiguration;
import com.hrznstudio.titanium.pulsar.config.IConfiguration;
import com.hrznstudio.titanium.pulsar.flightpath.Flightpath;
import com.hrznstudio.titanium.pulsar.flightpath.IExceptionHandler;
import com.hrznstudio.titanium.pulsar.flightpath.lib.AnnotationLocator;
import com.hrznstudio.titanium.pulsar.internal.BusExceptionHandler;
import com.hrznstudio.titanium.pulsar.internal.CrashHandler;
import com.hrznstudio.titanium.pulsar.pulse.Pulse;
import com.hrznstudio.titanium.pulsar.pulse.PulseMeta;
import joptsimple.internal.Strings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.CrashReportExtender;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manager class for a given mods Pulses.
 * <p>
 * This MUST be constructed by a mod BEFORE preinit as it registers on to the mod event bus - a static block would serve
 * for this. No more Pulses can be registered after preinit has been caught, so assume preinit is too late to register
 * new Pulses.
 * <p>
 * Each Pulsar-enabled mod should create one and only one of these to manage its Pulses.
 *
 * @author Arkan <arkan@drakon.io>
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class PulseManager {

    private final boolean useConfig;
    private final LinkedHashMap<Object, PulseMeta> pulses = new LinkedHashMap<>();
    // Use the Google @Subscribe to avoid confusion/breaking changes.
    private final Flightpath flightpath = new Flightpath(new AnnotationLocator(Subscribe.class));
    private Logger log;

    private boolean blockNewRegistrations = false;
    private boolean configLoaded = false;
    private IConfiguration conf;
    private String id;
    private String name;

    /**
     * Configuration-using constructor.
     * <p>
     * This form creates a PulseManager that supports configuration of Pulses by file.
     *
     * @param configName The config file name.
     */
    public PulseManager(String configName) {
        this(new ForgeConfiguration(configName));
    }

    /**
     * Custom configuration-using constructor.
     * <p>
     * Don't like JSON? Heathen. Lets you handle configuration, to whatever media you like - File, database, death star.
     * Whatever really. See {@link com.hrznstudio.titanium.pulsar.config.IConfiguration}.
     *
     * @param config Configuration handler.
     */
    public PulseManager(IConfiguration config) {
        init();
        useConfig = true;
        conf = config;
    }

    /**
     * Shared initialiser code between all the constructors.
     */
    private void init() {
        String modId = FMLModLoadingContext.get().getActiveContainer().getModId();
        String modName = FMLModLoadingContext.get().getActiveContainer().getModInfo().getDisplayName();
        this.id = modId;
        this.name = Strings.isNullOrEmpty(modName) ? modId : modName;
        log = LogManager.getLogger(modName + "|Pulsar");
        flightpath.setExceptionHandler(new BusExceptionHandler(modId));
        CrashReportExtender.registerCrashCallable(new CrashHandler(modId, this));
        // Attach us to the mods FML bus
        attachToContainerEventBus(this);
    }

    /**
     * Overrides Pulsars default behaviour when a pulse emits an exception. See Flightpath's documentation.
     *
     * @param handler The Flightpath-compatible exception handler to use.
     */
    public void setPulseExceptionHandler(IExceptionHandler handler) {
        flightpath.setExceptionHandler(handler);
    }

    /**
     * Register a new Pulse with the manager.
     * <p>
     * This CANNOT be done after preinit has been invoked.
     *
     * @param pulse The Pulse to register.
     */
    public void registerPulse(Object pulse) {
        if (blockNewRegistrations) throw new RuntimeException("A mod tried to register a plugin after preinit! Pulse: "
                + pulse);
        if (!configLoaded) {
            conf.load();
            configLoaded = true;
        }

        String id, description, deps, pulseDeps;
        boolean forced, enabled, defaultEnabled, missingDeps = false;

        try {
            Pulse p = pulse.getClass().getAnnotation(Pulse.class);
            id = p.id();
            description = p.description();
            deps = p.modsRequired();
            pulseDeps = p.pulsesRequired();
            forced = p.forced();
            enabled = p.defaultEnable();
            defaultEnabled = p.defaultEnable();
        } catch (NullPointerException ex) {
            throw new RuntimeException("Could not parse @Pulse annotation for Pulse: " + pulse);
        }

        // Work around Java not allowing default-null fields.
        if (description.equals("")) description = null;

        if (!deps.equals("")) {
            String[] parsedDeps = deps.split(";");
            for (String s : parsedDeps) {
                //TODO:
                /*if (!FMLModLoadingContext.get().isModLoaded(s)) {
                    log.info("Skipping Pulse " + id + "; missing dependency: " + s);
                    missingDeps = true;
                    enabled = false;
                    break;
                }*/
            }
        }

        PulseMeta meta = new PulseMeta(id, description, forced, enabled, defaultEnabled);
        meta.setMissingDeps(missingDeps || !hasRequiredPulses(meta, pulseDeps));
        meta.setEnabled(getEnabledFromConfig(meta));

        if (meta.isEnabled()) {
            pulses.put(pulse, meta);
            flightpath.register(pulse);

            MinecraftForge.EVENT_BUS.register(pulse);
        }
    }

    /**
     * Helper to attach a given object to the mod container event bus.
     *
     * @param obj Object to register.
     */
    private void attachToContainerEventBus(Object obj) {
        /*
        ModContainer cnt = Loader.instance().activeModContainer();
        log.debug("Attaching [" + obj + "] to event bus for container [" + cnt + "]");
        try {
            FMLModContainer mc = (FMLModContainer) cnt;
            Field ebf = mc.getClass().getDeclaredField("eventBus");

            boolean access = ebf.isAccessible();
            ebf.setAccessible(true);
            EventBus eb = (EventBus) ebf.get(mc);
            ebf.setAccessible(access);

            eb.register(obj);
        } catch (NoSuchFieldException nsfe) {
            throw new RuntimeException("Pulsar >> Incompatible FML mod container (missing eventBus field) - wrong Forge version?");
        } catch (IllegalAccessException iae) {
            throw new RuntimeException("Pulsar >> Security Manager blocked access to eventBus on mod container. Cannot continue.");
        } catch (ClassCastException cce) {
            throw new RuntimeException("Pulsar >> Something in the mod container had the wrong type? " + cce.getMessage());
        }*/
        //TODO Rewrite this to new event bus
    }

    /**
     * Internal (but public for EventBus use) handler for events.
     * <p>
     * DO NOT CALL THIS DIRECTLY! Let EventBus handle it.
     *
     * @param evt An event object.
     */
    @Subscribe
    public void propagateEvent(Object evt) {
        if (evt instanceof FMLPreInitializationEvent) preInit((FMLPreInitializationEvent) evt);
        // We use individual buses due to the EventBus class using a Set rather than a List, thus losing the ordering.
        // This trick is shamelessly borrowed from FML.
        flightpath.post(evt);
    }

    private boolean getEnabledFromConfig(PulseMeta meta) {
        if (meta.isForced() || !useConfig) return true; // Forced or no config set.

        return conf.isModuleEnabled(meta);
    }

    private void preInit(FMLPreInitializationEvent evt) {
        if (!blockNewRegistrations) conf.flush(); // First preInit call, so flush config
        blockNewRegistrations = true;
    }

    private boolean hasRequiredPulses(PulseMeta meta, String deps) {
        if (!deps.equals("")) {
            String[] parsedDeps = deps.split(";");
            for (String s : parsedDeps) {
                if (!isPulseLoaded(s)) {
                    log.info("Skipping Pulse " + meta.getId() + "; missing pulse: " + s);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if a given Pulse ID is loaded in this manager.
     *
     * @param pulseId The ID to check.
     * @return Whether the ID was present.
     */
    public boolean isPulseLoaded(String pulseId) {
        for (Map.Entry<Object, PulseMeta> entry : pulses.entrySet()) {
            if (entry.getValue().getId().equals(pulseId)) {
                return true;
            }
        }
        return false;
    }

    public Collection<PulseMeta> getAllPulseMetadata() {
        return pulses.values();
    }

    @Override
    public String toString() {
        return "PulseManager[" + id + "]";
    }
}
