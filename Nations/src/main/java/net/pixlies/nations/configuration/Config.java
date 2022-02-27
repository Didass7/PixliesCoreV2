package net.pixlies.nations.configuration;

import net.pixlies.nations.Nations;
import net.pixlies.nations.handlers.Handler;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Config extends YamlConfiguration implements Handler {

    private static final Nations instance = Nations.getInstance();

    private final File file;
    private final String localDefaultsName;

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
