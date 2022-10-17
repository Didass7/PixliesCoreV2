package net.pixlies.core.utils;

import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.moderation.Punishment;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class PunishmentUtils {

    public static final String DEFAULT_REASON = Main.getInstance().getConfig().getString("moderation.defaultReason", "No reason given.");

    private PunishmentUtils() {

    }

    public static boolean isSilent(@Nullable String string) {
        if (string != null && !string.isEmpty()) {
            return string.endsWith("-s") || string.startsWith("-s");
        }
        return false;
    }

    public static UUID getPunisherUUID(CommandSender sender) {
        return PlayerUtils.getIdFromSender(sender, PlayerUtils.getConsoleUUID());
    }

    public static String replaceReason(String reason) {

        if (reason == null || reason.isEmpty()) {
            return DEFAULT_REASON;
        }

        return reason.replace("-s", "");
    }

}
