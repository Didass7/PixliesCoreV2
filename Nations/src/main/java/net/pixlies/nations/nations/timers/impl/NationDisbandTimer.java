package net.pixlies.nations.nations.timers.impl;

import lombok.Getter;
import net.pixlies.core.entity.user.timers.Timer;
import org.bukkit.ChatColor;

public class NationDisbandTimer extends Timer {

    public static final String ID = "nation_disband";
    private final @Getter String nationToDisband;

    public NationDisbandTimer(long startTime, String nationToDisband) {
        super(ChatColor.RED, "Disband", startTime, 30000); // 30 seconds
        this.nationToDisband = nationToDisband;
    }

}
