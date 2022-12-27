package net.pixlies.core.utils;

import org.bukkit.entity.Player;

public class ChatUtils {

    public static String formatByPerm(Player player, String message) {
        return player.hasPermission("pixlies.chat.formatting") ? CC.format(message) : message;
    }

}
