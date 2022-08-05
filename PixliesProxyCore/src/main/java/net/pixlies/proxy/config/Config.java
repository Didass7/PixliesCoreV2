package net.pixlies.proxy.config;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.pixlies.proxy.PixliesProxy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * some bungee config
 * @author Dynmie
*/
public class Config {

    private static final PixliesProxy instance = PixliesProxy.getInstance();

    @Getter private Configuration config = new Configuration();
    private final File file;
    private final String localDefaults;

    public Config(File file, String localDefaults) {
        this.file = file;
        this.localDefaults = localDefaults;
        createIfNotExists();
        reload();
    }

    public Config(File file) {
        this(file, null);
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().warning("Failed to save file " + file.getName() + ".");
        }
    }

    public void reload() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().warning("Failed to load file " + file.getName() + ".");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createIfNotExists() {

        file.getParentFile().mkdirs();

        if (file.exists()) return;

        if (localDefaults == null) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (InputStream input = instance.getResourceAsStream(localDefaults)) {
            file.createNewFile();
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}