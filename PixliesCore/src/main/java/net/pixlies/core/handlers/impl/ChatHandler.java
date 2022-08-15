package net.pixlies.core.handlers.impl;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.handlers.Handler;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatHandler implements Handler {

    private static final Main instance = Main.getInstance();
    private final Config config = instance.getConfig();

    @Getter @Setter private volatile boolean muted = false;

    @Getter private volatile long slowMode = instance.getSettings().getLong("chat.slowmode", 0L);
    private final Map<UUID, Long> slowModePlayers = new ConcurrentHashMap<>();

    public void setSlowMode(long value) {
        slowMode = value;
        instance.getSettings().set("chat.slowmode", value);
        instance.getSettings().save();
    }

    public long getPlayerCooldown(UUID uuid) {
        if (slowModePlayers.get(uuid) == null) {
            return 0;
        } else {
            return slowModePlayers.get(uuid);
        }
    }

    public long getPlayerCooldownInSeconds(UUID uuid) {
        long now = System.currentTimeMillis();
        long lastSent = getPlayerCooldown(uuid);
        long until = now - lastSent;

        return lastSent + until - now;
    }

    public boolean isPlayerOnCooldown(UUID uuid) {
        long now = System.currentTimeMillis();
        long lastSent = getPlayerCooldown(uuid);
        long until = now - lastSent;

        boolean cooldown = false;

        if (slowMode < until) {
            slowModePlayers.remove(uuid);
            cooldown = true;
        }

        return cooldown;
    }

    public void setPlayerCooldown(UUID uuid, long value) {
        slowModePlayers.remove(uuid);
        slowModePlayers.put(uuid, value);
    }

}
