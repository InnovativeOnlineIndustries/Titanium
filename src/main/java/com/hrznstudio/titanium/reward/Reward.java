/*
 * This file is part of Titanium
 * Copyright (C) 2025, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.reward;

import com.google.gson.JsonParser;
import com.hrznstudio.titanium.util.URLUtil;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Reward {

    public static Calendar BUUZ_BIRTH_DATE = Calendar.getInstance();

    static {
        BUUZ_BIRTH_DATE.set(BUUZ_BIRTH_DATE.get(Calendar.YEAR), Calendar.FEBRUARY, 12);
    }

    private final ResourceLocation resourceLocation;
    private final URL contributorsURL;
    private String unlocalizedName;
    private List<UUID> players;
    private Supplier<Consumer<Dist>> register;
    private String[] options;

    public Reward(ResourceLocation resourceLocation, URL contributorsURL, Supplier<Consumer<Dist>> register, String[] options) {
        this.resourceLocation = resourceLocation;
        this.contributorsURL = contributorsURL;
        this.players = new ArrayList<>();
        this.register = register;
        this.options = options;
        new Thread(() -> {
            this.players = getPlayers(contributorsURL);
        }).start();
    }

    private static List<UUID> getPlayers(URL url) {
        try {
            List<UUID> players = new ArrayList<>();
            new JsonParser().parse(URLUtil.readUrl(url)).getAsJsonObject().get("uuid").getAsJsonArray().forEach(jsonElement -> players.add(UUID.fromString(jsonElement.getAsString())));
            return players;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Reward setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    public void register(Dist dist) {
        this.register.get().accept(dist);
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public String[] getOptions() {
        return options;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public boolean isPlayerValid(UUID uuid) {
        if (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) == BUUZ_BIRTH_DATE.get(Calendar.WEEK_OF_YEAR))
            return true;
        return players.contains(uuid);
    }
}
