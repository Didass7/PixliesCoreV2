package net.pixlies.core.entity.user.timers.impl;

import net.pixlies.core.entity.user.timers.Timer;
import org.bukkit.ChatColor;

public class TeleportTimer extends Timer {

    public static final String ID = "teleport";
    public static final long TELEPORT_DELAY = 5000;

    public TeleportTimer(long startTime) {
        super(ChatColor.YELLOW, "Teleport", startTime, TELEPORT_DELAY, false);
    }

}
