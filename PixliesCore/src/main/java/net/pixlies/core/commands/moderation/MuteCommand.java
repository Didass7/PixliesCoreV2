package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.PunishmentUtils;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class MuteCommand extends BaseCommand {

    @CommandAlias("mute")
    @CommandPermission("pixlies.moderation.mute")
    @CommandCompletion("@players")
    @Syntax("<player> [reason] [-s]")
    @Description("Mutes player with the default reason")
    public void onMute(CommandSender sender, OfflinePlayer target, @Optional String reason) {
        User user = User.getActiveUser(target.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (!user.isPunishmentsLoaded()) {
                Lang.FETCHING.send(sender);
                user.loadPunishments();
            }
            if (user.isMuted()) {
                Lang.PLAYER_ALREADY_MUTED.send(sender);
                return;
            }
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                user.mute(
                        PunishmentUtils.replaceReason(reason),
                        sender,
                        PunishmentUtils.isSilent(reason)
                );
                user.savePunishments();
            });
        });
    }

}
