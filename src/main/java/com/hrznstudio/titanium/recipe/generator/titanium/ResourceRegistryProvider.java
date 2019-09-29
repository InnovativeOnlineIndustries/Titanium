package com.hrznstudio.titanium.recipe.generator.titanium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceRegistryProvider implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;

    public ResourceRegistryProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        ResourceRegistry.getMaterials().forEach(material -> {
            material.getGenerated().values().stream().filter(entry -> entry instanceof IJSONGenerator && entry instanceof IJsonFile).forEach(entry -> {
                Path output = path.resolve((((IJsonFile) entry).getRecipeSubfolder() != null ? ((IJsonFile) entry).getRecipeSubfolder() : ""));
                try {
                    Files.createDirectories(output);
                    try (BufferedWriter bufferedwriter = Files.newBufferedWriter(output.resolve(((IJsonFile) entry).getRecipeKey() + ".json"))) {
                        bufferedwriter.write(GSON.toJson(((IJSONGenerator) entry).generate()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public String getName() {
        return "Resource Registry";
    }
}
