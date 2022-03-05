package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TimeUnit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class TempMuteCommand extends BaseCommand {

    @CommandAlias("tempmute")
    @CommandPermission("pixlies.moderation.tempmute")
    @CommandCompletion("@players")
    @Description("Temporarily mutes player with the default reason")
    public void onTempMute(CommandSender sender, OfflinePlayer target, String duration, @Optional String reason) {
        boolean silent = false;
        long durationLong = TimeUnit.getDuration(duration);
        String muteReason = reason;
        if (reason != null && !reason.isEmpty()) {
            muteReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        User user = User.get(target.getUniqueId());

        if (user.isMuted()) {
            Lang.PLAYER_ALREADY_MUTED.send(sender);
            return;
        }

        user.tempMute(muteReason, sender, durationLong, silent);
    }

}
