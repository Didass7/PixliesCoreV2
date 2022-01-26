package net.pixlies.core.configuration;

import net.pixlies.core.Main;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Config extends YamlConfiguration {

    private static final Main instance = Main.getInstance();

    private final File file;
    private final String localDefaultsName;

    /**
     * Nice config.
     * @param file The path where you want to save your config.
     * @param localDefaultsName The local file in the jar file.
     */
    public Config(File file, String localDefaultsName) {
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

    public Config(File file) {
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

}
