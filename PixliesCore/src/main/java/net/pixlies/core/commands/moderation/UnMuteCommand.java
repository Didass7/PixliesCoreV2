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

public class UnMuteCommand extends BaseCommand {

    @CommandAlias("unmute|mutent")
    @CommandPermission("pixlies.moderation.unmute")
    @CommandCompletion("@players")
    @Description("Mute'nt a player")
    @Syntax("<player> [-s]")
    public void onUnmute(CommandSender sender, OfflinePlayer target, @Optional String s) {
        User user = User.getActiveUser(target.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (!user.isPunishmentsLoaded()) {
                Lang.FETCHING.send(sender);
                user.loadPunishments();
            }
            if (!user.isMuted()) {
                Lang.PLAYER_NOT_MUTED.send(sender);
                return;
            }
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                user.unmute(sender, PunishmentUtils.isSilent(s));
                user.savePunishments();
            });
        });
    }

}
