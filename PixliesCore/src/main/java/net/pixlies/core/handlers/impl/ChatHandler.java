package net.pixlies.core.handlers.impl;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.handlers.Handler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatHandler implements Handler {

    private static final Main instance = Main.getInstance();
    private final Config settings = instance.getSettings();

    @Getter @Setter private volatile boolean muted = false;

    @Getter private volatile long slowModeDelay = settings.getLong("chat.slowmode", 0L);
    private final Map<UUID, Long> slowModePlayers = new ConcurrentHashMap<>();

    public void setSlowMode(UUID uuid, boolean state) {
        if (state) {
            slowModePlayers.remove(uuid);
            slowModePlayers.put(uuid, System.currentTimeMillis());
        } else {
            slowModePlayers.remove(uuid);
        }
    }

    public void setSlowModeDelay(long slowModeDelay) {
        slowModeDelay = slowModeDelay * 1000;
        // Seconds to millis
        settings.set("chat.slowmode", slowModeDelay);
        settings.save();
        this.slowModeDelay = slowModeDelay;
    }

    public long getSlowModeDelayAsSeconds() {
        return slowModeDelay / 1000;
    }

    public boolean isSlowed(UUID uuid) {

        long lastTalkTime = slowModePlayers.getOrDefault(uuid, 0L);
        long delay = slowModeDelay;
        long currentTime = System.currentTimeMillis();

        return lastTalkTime + delay >= currentTime;

    }

    public long getLastChat(UUID uuid) {
        return slowModePlayers.getOrDefault(uuid, 0L);
    }

}
