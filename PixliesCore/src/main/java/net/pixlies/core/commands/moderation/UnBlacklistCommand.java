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

/**
 * Unblacklist a player, created because i blacklisted myself
 * @author Dynmie
 */
public class UnBlacklistCommand extends BaseCommand {

    @CommandAlias("unblacklist|blacklistnt")
    @CommandPermission("pixlies.moderation.unblacklist")
    @CommandCompletion("@players")
    @Syntax("<player> [-s]")
    @Description("Unblacklist a player")
    public void onUnBlacklist(CommandSender sender, OfflinePlayer target, @Optional String s) {
        User user = User.getActiveUser(target.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (!user.isPunishmentsLoaded()) {
                Lang.FETCHING.send(sender);
                user.loadPunishments();
            }

            if (!user.isBlacklisted()) {
                Lang.PLAYER_NOT_BLACKLISTED.send(sender);
                return;
            }
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                user.unblacklist(sender, PunishmentUtils.isSilent(s));
                user.savePunishments();
            });
        });
    }

}
