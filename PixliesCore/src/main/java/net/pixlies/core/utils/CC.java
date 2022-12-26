package net.pixlies.core.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class CC {

    public static final String CHAT_STRIKETHROUGH = ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯";

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', formatHex(message));
    }

    @SuppressWarnings("deprecation") // legacy ChatColor
    public static String formatHex(String message) {
        String[] prefixHexParts = message.split("(?=&#[0-f]{6})");

        StringBuilder messages = new StringBuilder();

        for (String part : prefixHexParts){
            if(!part.matches("^(?=&#[0-f]{6}).*$")) {
                messages.append(part);
                continue;
            }

            part = part.substring(1);

            String hex = part.substring(0,7);
            part = part.substring(7);

            messages.append(net.md_5.bungee.api.ChatColor.of(hex).toString()).append(part);
        }

        return messages.toString();
    }

    public static String setPlaceholders(Player player, String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }

}
