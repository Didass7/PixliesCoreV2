package net.pixlies.proxy.config;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.pixlies.proxy.Proxy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {

    // TODO: Set defaults

    private static final Proxy instance = Proxy.getInstance();

    @Getter private Configuration config;
    private final File file;
    private final String localDefaults;

    /**
     * some bungee config
     * @param file The path where you want to save your config.
     * @param localDefaults The defaults in the resources folder
     */
    public Config(File file, String localDefaults) {
        this.file = file;
        this.localDefaults = localDefaults;
        createIfNotExists();
        reload();
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

    public void createIfNotExists() {
        if (file.exists()) return;
        try (InputStream input = instance.getResourceAsStream(localDefaults)) {
            Files.copy(input, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
