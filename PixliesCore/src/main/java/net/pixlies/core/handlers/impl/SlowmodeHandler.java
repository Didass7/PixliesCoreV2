package net.pixlies.core.handlers.impl;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.handlers.Handler;

import java.util.HashMap;
import java.util.UUID;

public class SlowmodeHandler implements Handler {

    @Getter @Setter Long slowmode = 0L;

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public Long getCooldown(UUID player) {
        return cooldowns.get(player) == null ? Long.valueOf(0) : cooldowns.get(player);
    }

    public void setCooldown(UUID player, Long seconds) {
        if (seconds == 0L) {
            cooldowns.remove(player);
        } else {
            cooldowns.put(player, seconds);
        }
    }

}
