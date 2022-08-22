package net.pixlies.core.entity.user.timers.impl;

import lombok.Getter;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.timers.Timer;
import org.bukkit.ChatColor;

import java.util.UUID;

public class RightClickDelayTimer extends Timer {

    public static final String ID = "rightClickDelay";

    private final @Getter UUID uuid;

    public RightClickDelayTimer(UUID uuid, long startTime) {
        super(ChatColor.BLACK, "Right Click Delay", startTime, 500, true); // 0.5 seconds
        this.uuid = uuid;
    }

    public void registerTimer() {
        User user = User.get(uuid);
        user.getAllTimers().put(ID, this);
    }

    public static boolean isExpired(User user) {
        Timer timer = user.getAllTimers().get(ID);
        if (timer == null) return true;
        if (timer.isExpired()) {
            user.getAllTimers().remove(ID);
            return true;
        }
        return false;
    }

}
