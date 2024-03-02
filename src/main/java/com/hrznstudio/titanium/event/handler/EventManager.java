/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.event.handler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EventManager {

    public static <T extends Event> FilteredEventManager<T> forge(Class<T> clazz) {
        return create(clazz, EventPriority.NORMAL, Bus.FORGE);
    }

    public static <T extends Event> FilteredEventManager<T> mod(Class<T> clazz) {
        return create(clazz, EventPriority.NORMAL, Bus.MOD);
    }

    public static <T extends Event> FilteredEventManager<T> mod(Class<T> clazz, EventPriority priority) {
        return create(clazz, priority, Bus.MOD);
    }

    public static <T extends Event> FilteredEventManager<T> forge(Class<T> clazz, EventPriority priority) {
        return create(clazz, priority, Bus.FORGE);
    }

    public static <T extends Event> FilteredEventManager<T> create(Class<T> clazz, Bus bus) {
        return create(clazz, EventPriority.NORMAL, bus);
    }

    public static <T extends Event> FilteredEventManager<T> create(Class<T> clazz, EventPriority priority, Bus bus) {
        return new FilteredEventManager<>(clazz, bus, priority);
    }

    public enum Bus {
        FORGE(() -> MinecraftForge.EVENT_BUS),
        MOD(() -> FMLJavaModLoadingContext.get().getModEventBus());

        private final Supplier<IEventBus> busSupplier;

        Bus(final Supplier<IEventBus> eventBusSupplier) {
            this.busSupplier = eventBusSupplier;
        }

        public IEventBus bus() {
            return busSupplier.get();
        }
    }

    public static <T extends GenericEvent<? extends F>, F> GenericEventManager<T, F> forgeGeneric(Class<T> clazz, Class<F> generic) {
        return createGeneric(clazz, EventPriority.NORMAL, Bus.FORGE, generic);
    }

    public static <T extends GenericEvent<? extends F>, F> GenericEventManager<T, F> modGeneric(Class<T> clazz, Class<F> generic) {
        return createGeneric(clazz, EventPriority.NORMAL, Bus.MOD, generic);
    }

    public static <T extends GenericEvent<? extends F>, F> GenericEventManager<T, F> modGeneric(Class<T> clazz, EventPriority priority, Class<F> generic) {
        return createGeneric(clazz, priority, Bus.MOD, generic);
    }

    public static <T extends GenericEvent<? extends F>, F> GenericEventManager<T, F> forgeGeneric(Class<T> clazz, EventPriority priority, Class<F> generic) {
        return createGeneric(clazz, priority, Bus.FORGE, generic);
    }

    public static <T extends GenericEvent<? extends F>, F> GenericEventManager<T, F> createGeneric(Class<T> clazz, Bus bus, Class<F> generic) {
        return createGeneric(clazz, EventPriority.NORMAL, bus, generic);
    }

    public static <T extends GenericEvent<? extends F>, F> GenericEventManager<T, F> createGeneric(Class<T> clazz, EventPriority priority, Bus bus, Class<F> generic) {
        return new GenericEventManager<>(clazz, bus, priority, generic);
    }

    public static interface ISubscribe {

        void subscribe();

    }

    public static class FilteredEventManager<T extends Event> implements ISubscribe {
        private Predicate<T> filter;
        private Consumer<T> process;
        private Class<T> event;
        private boolean cancel;
        private Bus bus;
        private EventPriority priority;

        public FilteredEventManager(Class<T> clazz, Bus bus, EventPriority priority) {
            this.event = clazz;
            this.filter = t -> true;
            this.process = t -> {
            };
            this.bus = bus;
            this.priority = priority;
        }

        public void subscribe() {
            bus.bus().addListener(priority, false, event, event -> {
                if (event.getClass().isAssignableFrom(this.event)) {
                    if (this.filter.test(event)) {
                        if (this.cancel) {
                            if (event.isCancelable()) {
                                event.setCanceled(true);
                            }
                        }
                        this.process.accept(event);
                    }
                }
            });
        }

        public FilteredEventManager<T> filter(Predicate<T> predicateFilter) {
            this.filter = this.filter.and(predicateFilter);
            return this;
        }

        public FilteredEventManager<T> process(Consumer<T> process) {
            this.process = this.process.andThen(process);
            return this;
        }

        public FilteredEventManager<T> cancel() {
            this.cancel = true;
            return this;
        }
    }

    public static class GenericEventManager<T extends GenericEvent<? extends F>, F> implements ISubscribe {
        private Predicate<T> filter;
        private Consumer<T> process;
        private Class<T> event;
        private boolean cancel;
        private Bus bus;
        private EventPriority priority;
        private Class<F> generic;

        public GenericEventManager(Class<T> clazz, Bus bus, EventPriority priority, Class<F> generic) {
            this.event = clazz;
            this.filter = t -> true;
            this.process = t -> {
            };
            this.bus = bus;
            this.priority = priority;
            this.generic = generic;
        }

        public void subscribe() {
            bus.bus().addGenericListener(this.generic, priority, false, this.event, event -> {
                if (event.getClass().isAssignableFrom(this.event)) {
                    if (this.filter.test(event)) {
                        if (this.cancel) {
                            if (event.isCancelable()) {
                                event.setCanceled(true);
                            }
                        }
                        this.process.accept(event);
                    }
                }
            });
        }

        public GenericEventManager<T, F> filter(Predicate<T> predicateFilter) {
            this.filter = this.filter.and(predicateFilter);
            return this;
        }

        public GenericEventManager<T, F> process(Consumer<T> process) {
            this.process = this.process.andThen(process);
            return this;
        }

        public GenericEventManager<T, F> cancel() {
            this.cancel = true;
            return this;
        }
    }
}
