package net.pixlies.core.utils;

import net.pixlies.core.localization.Lang;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;

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

    public static String generateId(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Get the location in a format
     * @author dynmie
     */
    public static String getLocationFormatted(Location location) {

        String format = Lang.LOCATION_FORMAT.getRaw("ENG");

        String world = location.getWorld().getName();
        String xPos = String.valueOf(location.getX());
        String yPos = String.valueOf(location.getY());
        String zPos = String.valueOf(location.getZ());

        return format.replace("%WORLD%", world)
                .replace("%X%", xPos)
                .replace("%Y%", yPos)
                .replace("%Z%", zPos);

    }

}
