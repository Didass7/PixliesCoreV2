package net.pixlies.core.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;

public class TextUtils {

    public static String getGameModeFormatted(GameMode mode) {
        return switch(mode) {
            case CREATIVE -> "§bCreative";
            case SURVIVAL -> "§cSurvival";
            case ADVENTURE -> "§6Adventure";
            case SPECTATOR -> "§3Spectator";
            default -> "§9" + StringUtils.capitalize(mode.name().toLowerCase());
        };
    }

}
