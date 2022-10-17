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
 * cool blacklist command for your enemies
 * @author Dynmie
 */
public class BlacklistCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @CommandAlias("blacklist")
    @CommandPermission("pixlies.moderation.blacklist")
    @CommandCompletion("@players")
    @Description("Bans player with the default reason")
    @Syntax("<player> [reason] [-s]")
    public void onBlacklist(CommandSender sender, OfflinePlayer target, @Optional String reason) {
        User user = User.getActiveUser(target.getUniqueId());

        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            if (!user.isPunishmentsLoaded()) {
                Lang.FETCHING.send(sender);
                user.loadPunishments();
            }
            if (user.isBlacklisted()) {
                Lang.PLAYER_ALREADY_BLACKLISTED.send(sender);
                return;
            }
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                user.blacklist(
                        PunishmentUtils.replaceReason(reason),
                        sender,
                        PunishmentUtils.isSilent(reason)
                );
                user.savePunishments();
            });
        });
    }

}
