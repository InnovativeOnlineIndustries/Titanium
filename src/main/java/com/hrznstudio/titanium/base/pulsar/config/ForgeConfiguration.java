package com.hrznstudio.titanium.base.pulsar.config;

import com.hrznstudio.titanium.base.pulsar.pulse.PulseMeta;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.Locale;

public class ForgeConfiguration implements IConfiguration {

    private final String confPath;
    private final String description;
    private Configuration config;

    public ForgeConfiguration(String confName, String description) {
        this.confPath = Loader.instance().getConfigDir().toString() + File.separator + confName + ".cfg";
        this.description = description.toLowerCase(Locale.ENGLISH);
    }

    public ForgeConfiguration(String confName) {
        this(confName, "Modules");
    }

    @Override
    public void load() {
        if (config == null)
            config = new Configuration(new File(this.confPath), "1");
        config.load();
    }

    @Override
    public boolean isModuleEnabled(PulseMeta meta) {
        Property prop = config.get(this.description, meta.getId(), meta.isDefaultEnabled(), meta.getDescription());
        prop.setRequiresMcRestart(true);
        return prop.getBoolean(meta.isEnabled());
    }

    @Override
    public void flush() {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
