package net.pixlies.proxy.handlers.impl;

import co.aikar.commands.annotation.Dependency;
import lombok.val;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaintenanceHandler implements Handler {

    private static final Proxy instance = Proxy.getInstance();

    private final Config config = instance.getConfig();
    private final Config settings = instance.getSettingsConfig();
    private final List<UUID> whitelisted = new ArrayList<>();

    public MaintenanceHandler() {
        reload();
    }

    public void save() {
        val whitelistedPlayers = new ArrayList<String>();
        whitelisted.forEach(uuid -> whitelistedPlayers.add(uuid.toString()));
        settings.getConfig().set("maintenance.whitelist", whitelistedPlayers);
        settings.save();
    }

    public void reload() {
        val whitelistedPlayers = settings.getConfig().getStringList("maintenance.whitelist");
        val whitelisted = new ArrayList<UUID>();
        for (String string : whitelistedPlayers) {
            try {
                UUID uuid = UUID.fromString(string);
                whitelisted.add(uuid);
            } catch (IllegalArgumentException ignored) {
                // IGNORED
            }
        }
        this.whitelisted.addAll(whitelisted);
    }

    public void addPlayer(UUID uuid) {
        whitelisted.add(uuid);
        save();
    }

    public void removePlayer(UUID uuid) {
        if (whitelisted.isEmpty()) return;
        whitelisted.remove(uuid);
        save();
    }

    public boolean isWhitelisted(UUID uuid) {
        if (whitelisted.isEmpty()) return false;
        return whitelisted.contains(uuid);
    }

    public boolean isEnabled() {
        return config.getConfig().getBoolean("maintenance.enabled", false);
    }

}
