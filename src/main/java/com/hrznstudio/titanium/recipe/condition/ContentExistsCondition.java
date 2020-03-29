package com.hrznstudio.titanium.recipe.condition;

import com.hrznstudio.titanium.Titanium;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.IForgeRegistry;

public class ContentExistsCondition implements ICondition {
    public static final ResourceLocation NAME = new ResourceLocation(Titanium.MODID, "content_exists");

    private final IForgeRegistry<?> forgeRegistry;
    private final ResourceLocation contentName;

    public ContentExistsCondition(IForgeRegistry<?> forgeRegistry, ResourceLocation contentName) {
        this.forgeRegistry = forgeRegistry;
        this.contentName = contentName;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return forgeRegistry.containsKey(contentName);
    }

    public IForgeRegistry<?> getForgeRegistry() {
        return forgeRegistry;
    }

    public ResourceLocation getContentName() {
        return this.contentName;
    }
}
