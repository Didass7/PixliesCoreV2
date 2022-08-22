package net.pixlies.core.entity.user.timers.impl;

import net.pixlies.core.entity.user.timers.Timer;
import org.bukkit.ChatColor;

public class CombatTimer extends Timer {
    public static final String ID = "combat";

    public CombatTimer(long startTime) {
        super(ChatColor.DARK_RED, "Combat", startTime, 60000, false); // 1 minute
    }
}
