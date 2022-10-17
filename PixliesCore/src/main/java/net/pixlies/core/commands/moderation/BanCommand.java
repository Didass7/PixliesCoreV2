package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.PunishmentUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class BanCommand extends BaseCommand {

    @CommandAlias("ban")
    @CommandPermission("pixlies.moderation.ban")
    @CommandCompletion("@players")
    @Description("Bans player with the default reason")
    public void onBan(CommandSender sender, OfflinePlayer target, @Optional String reason) {
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
                user.ban(
                        PunishmentUtils.replaceReason(reason),
                        sender,
                        PunishmentUtils.isSilent(reason)
                );
                user.savePunishments();
            });
        });

    }

}
