package net.pixlies.lobby.managers;

import net.pixlies.lobby.Lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GrapplingHookManager {

    private static final Lobby instance = Lobby.getInstance();

    private final Map<UUID, Long> grappleTimer = new HashMap<>();

    public static final long DELAY = 1300;

    public boolean isDelayed(UUID uuid) {
        if (!grappleTimer.containsKey(uuid)) return false;

        long currentTime = System.currentTimeMillis();
        long previousTime = grappleTimer.getOrDefault(uuid, (long) 0);

        long time = currentTime - previousTime;

        if (time < GrapplingHookManager.DELAY) {
            return true;
        }

        removeDelay(uuid);
        return false;

    }

    public void removeDelay(UUID uuid) {
        grappleTimer.remove(uuid);
    }

    public void delay(UUID uuid) {
        grappleTimer.put(uuid, System.currentTimeMillis());
    }

}
