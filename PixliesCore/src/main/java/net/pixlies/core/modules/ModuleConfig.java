package net.pixlies.core.modules;

import net.pixlies.core.utils.CC;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Cool little module config class
 * @author dynmie
 */
public class ModuleConfig extends YamlConfiguration {

    private final JavaPlugin instance;
    private final File file;
    private final String localDefaultsName;

    /**
     * Nice config.
     * @param file The path where you want to save your config.
     * @param localDefaultsName The local file in the jar file.
     */
    public ModuleConfig(JavaPlugin instance, File file, String localDefaultsName) {
        this.instance = instance;
        this.file = file;
        this.localDefaultsName = localDefaultsName;
        try {
            Reader stream = new InputStreamReader(Objects.requireNonNull(instance.getResource(localDefaultsName)), StandardCharsets.UTF_8);
            YamlConfiguration config = new YamlConfiguration();
            config.load(stream);
            this.setDefaults(config);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            instance.getLogger().warning("Failed to load defaults for " + file.getName() + ".");
        }
        createIfNotExists();
    }

    public ModuleConfig(JavaPlugin instance, File file) {
        this.instance = instance;
        this.file = file;
        this.localDefaultsName = null;
        createIfNotExists();
    }

    public void save() {
        try {
            super.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().warning("Failed to save file " + file.getName() + ".");
        }
    }

    public void reload() {
        try {
            super.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            instance.getLogger().warning("Failed to load file " + file.getName() + ".");
        }
    }

    public void createIfNotExists() {
        if (file.exists()) {
            reload();
            return;
        }

        if (localDefaultsName == null) {
            save();
            return;
        }

        try {
            FileUtils.copyInputStreamToFile(Objects.requireNonNull(instance.getResource(localDefaultsName)), file);
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().warning("Failed to create " + file.getName() + ".");
        }
    }

    public String getStringFormatted(String key) {
        return CC.format(getString(key));
    }

    public String getStringFormatted(String key, String defaults) {
        return CC.format(getString(key, defaults));
    }

}
