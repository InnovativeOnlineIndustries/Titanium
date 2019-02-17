/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.tab;

import com.google.common.base.Stopwatch;
import com.hrznstudio.titanium.util.TagUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AdvancedTitaniumTab extends TitaniumTab {
    private static final ItemStack defaultIconStack = new ItemStack(Blocks.BARRIER);
    private List<ItemStack> icons = new ArrayList<>();
    private ItemStack currentIcon;
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

    public void addIconStacks(Collection<ItemStack> icons) {
        this.icons.addAll(icons);
    }

    public void addIconStack(ItemStack icon) {
        this.icons.add(icon);
    }

    public void addIconStacks(ItemStack... icons) {
        Collections.addAll(this.icons, icons);
    }

    public void addIconStack(Tag<Item> tag) {
        this.icons.addAll(TagUtil.getAllEntries(tag).stream().map(ItemStack::new).collect(Collectors.toList()));
    }

    @Nonnull
    private ItemStack getCurrentIcon() {
        updateIcon();
        return currentIcon;
    }
}