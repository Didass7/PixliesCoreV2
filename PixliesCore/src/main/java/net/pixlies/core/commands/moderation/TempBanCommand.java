package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.PunishmentUtils;
import net.pixlies.core.utils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class TempBanCommand extends BaseCommand {

    @CommandAlias("tempban")
    @CommandPermission("pixlies.moderation.tempban")
    @CommandCompletion("@players")
    @Syntax("<player> <duration> [reason] [-s]")
    @Description("Temporarily bans player with the default reason")
    public void onTempBan(CommandSender sender, OfflinePlayer target, String duration, @Optional String reason) {
        long durationLong = TimeUnit.getDuration(duration);

        if (durationLong < 1) {
            Lang.NOT_A_DURATION.send(sender);
            return;
        }

        User user = User.getActiveUser(target.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (!user.isPunishmentsLoaded()) {
                Lang.FETCHING.send(sender);
                user.loadPunishments();
            }
            if (user.isBanned()) {
                Lang.PLAYER_ALREADY_BANNED.send(sender);
                return;
            }
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                user.tempBan(
                        PunishmentUtils.replaceReason(reason),
                        sender,
                        durationLong,
                        PunishmentUtils.isSilent(reason)
                );
                user.savePunishments();
            });
        });
    }

}
