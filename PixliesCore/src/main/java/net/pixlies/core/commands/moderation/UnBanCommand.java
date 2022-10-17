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

public class UnBanCommand extends BaseCommand {

    @CommandAlias("unban|bannt")
    @CommandPermission("pixlies.moderation.unban")
    @CommandCompletion("@players")
    @Description("Ban'nt a player")
    @Syntax("<player> [-s]")
    public void onUnban(CommandSender sender, OfflinePlayer target, @Optional String s) {
        User user = User.getActiveUser(target.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (!user.isPunishmentsLoaded()) {
                Lang.FETCHING.send(sender);
                user.loadPunishments();
            }

            if (!user.isBanned()) {
                Lang.PLAYER_NOT_BANNED.send(sender);
                return;
            }
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                user.unban(sender, PunishmentUtils.isSilent(s));
                user.savePunishments();
            });
        });

    }

}
