/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.tab;

import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class AdvancedTitaniumTab extends TitaniumTab {
    private static final Supplier<ItemStack> defaultIconStack = () -> new ItemStack(Blocks.BARRIER);
    private List<Supplier<ItemStack>> icons = new ArrayList<>();
    private Supplier<ItemStack> currentIcon;
    private int current;
    private Stopwatch watch;
    private Random random;
    private boolean shouldRandomise;

    public AdvancedTitaniumTab(String label, boolean randomise) {
        super(label, null);
        stackSupplier = this::getCurrentIcon;
        this.shouldRandomise = randomise;
        watch = Stopwatch.createStarted();
        random = new Random();
    }

    private void updateIcon() {
        if (icons.size() > 0) {
            if (this.watch.elapsed(TimeUnit.MILLISECONDS) >= 1500) {
                this.watch.reset().start();
                if (shouldRandomise) {
                    currentIcon = icons.get(random.nextInt(icons.size()));
                } else {
                    currentIcon = icons.get(current++);
                    if (current >= icons.size())
                        current = 0;
                }
            }
        } else {
            currentIcon = defaultIconStack;
        }
    }

    public void addIconStacks(Collection<Supplier<ItemStack>> icons) {
        this.icons.addAll(icons);
    }

    public void addIconStack(Supplier<ItemStack> icon) {
        this.icons.add(icon);
    }

    public void addIconStacks(Supplier<ItemStack>... icons) {
        Collections.addAll(this.icons, icons);
    }

    @Nonnull
    private ItemStack getCurrentIcon() {
        updateIcon();
        return currentIcon.get();
    }
}
