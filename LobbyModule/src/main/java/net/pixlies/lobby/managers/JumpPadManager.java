package net.pixlies.lobby.managers;

import lombok.Getter;
import net.pixlies.lobby.Lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JumpPadManager {

    private static final Lobby instance = Lobby.getInstance();

    private final Map<UUID, Long> jumpPadTimer = new HashMap<>();

    public static final long DELAY = 1000;

    public boolean isDelayed(UUID uuid) {
        if (!jumpPadTimer.containsKey(uuid)) return false;

        long currentTime = System.currentTimeMillis();
        long previousTime = jumpPadTimer.getOrDefault(uuid, (long) 0);

        long time = currentTime - previousTime;

        if (time < JumpPadManager.DELAY) {
            return true;
        }

        removeDelay(uuid);
        return false;

    }

    public void removeDelay(UUID uuid) {
        jumpPadTimer.remove(uuid);
    }

    public void delay(UUID uuid) {
        jumpPadTimer.put(uuid, System.currentTimeMillis());
    }

}
