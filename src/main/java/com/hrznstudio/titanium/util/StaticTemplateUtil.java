package com.hrznstudio.titanium.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.fml.packs.ResourcePackLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Full credits for this class to Immersive Engineering.
 * Source: https://github.com/BluSunrize/ImmersiveEngineering/blob/1.14/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/StaticTemplateManager.java
 *
 * IE is licensed under "Blu's License of Common Sense" as seen here:
 * @Link https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE
 */
public class StaticTemplateUtil {

    public static Optional<InputStream> getModResource(ResourcePackType type, ResourceLocation name) {
        return ModList.get().getMods().stream()
                .map(ModInfo::getModId)
                .map(ResourcePackLoader::getResourcePackFor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(mfrp -> mfrp.resourceExists(type, name))
                .map(mfrp -> getInputStreamOrThrow(type, name, mfrp))
                .findAny();
    }

    private static InputStream getInputStreamOrThrow(ResourcePackType type, ResourceLocation id, ModFileResourcePack source) {
        try {
            return source.getResourceStream(type, id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Template loadStaticTemplate(ResourceLocation id) throws IOException {
        Optional<InputStream> optStream = getModResource(ResourcePackType.SERVER_DATA,
                new ResourceLocation(id.getNamespace(), id.getPath() + ".nbt"));
        if (!optStream.isPresent()) {
            throw new RuntimeException("Mod resource not found: " + id);
        }
        return loadTemplate(optStream.get());
    }

    public static Template loadTemplate(InputStream inputStreamIn) throws IOException {
        CompoundNBT compoundnbt = CompressedStreamTools.readCompressed(inputStreamIn);
        Template template = new Template();
        template.read(compoundnbt);
        return template;
    }

}
