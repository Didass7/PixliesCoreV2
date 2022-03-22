package net.pixlies.proxy.handlers.impl;

import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.Handler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerListHandler implements Handler {

    private static final Proxy instance = Proxy.getInstance();

    private final Config config = instance.getConfig();

    public int getMaintenanceProtocol() {
        return config.getConfig().getInt("serverlist.maintenance.protocol", 2);
    }

    public @NotNull String getMaintenanceMessage() {
        return config.getConfig().getString("serverlist.maintenance.message", "");
    }

    public @NotNull String getVersionMessage() {
        return config.getConfig().getString("serverlist.version.message", "");
    }

    public @NotNull String getDescription() {
        return config.getConfig().getString("serverlist.list.description", "")
                .replace("\\n", "\n");
    }

    public boolean hasPlayerListMessage() {
        return config.getConfig().getString("serverlist.playerlist.message") != null;
    }

    public @Nullable String getPlayerListMessage() {
        return config.getConfig().getString("serverlist.playerlist.message");
    }

    public boolean isEnabled() {
        return config.getConfig().getBoolean("serverlist.enabled", true);
    }

}
