package com.hrznstudio.titanium.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonDataGenerator {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File DATA_DIR;
    private boolean dev;
    private List<IJsonFile> recipes = new ArrayList<>();
    private List<String> usedNames;

    public JsonDataGenerator(DataTypes type, String modid) {
        dev = new File("../src/main/resources/").exists();
        DATA_DIR = new File("../src/main/resources/data/" + modid + "/" + type.getFolderName());
        usedNames = new ArrayList<>();
        if (dev) {
            if (!DATA_DIR.exists()) DATA_DIR.mkdirs();
        }
    }

    public JsonDataGenerator addRecipe(IJsonFile recipe) {
        recipes.add(recipe);
        return this;
    }

    public void generate() {
        if (dev) {
            for (IJsonFile recipe : recipes) {
                try {
                    FileWriter writer = new FileWriter(new File(DATA_DIR, getUniqueName(recipe.getRecipeKey()) + ".json"));
                    GSON.toJson(recipe, writer);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getUniqueName(String name) {
        if (usedNames.contains(name)) {
            return getUniqueName(name + "_alt");
        }
        usedNames.add(name);
        return name;
    }

    public enum DataTypes {
        ADVANCEMENT("advancements"),
        LOOT("loot_tables"),
        RECIPE("recipes"),
        TAG("tags");

        private final String folderName;

        DataTypes(String folderName) {
            this.folderName = folderName;
        }

        public String getFolderName() {
            return folderName;
        }
    }
}
