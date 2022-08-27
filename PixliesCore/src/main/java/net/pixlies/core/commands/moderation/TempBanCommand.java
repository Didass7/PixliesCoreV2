package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class TempBanCommand extends BaseCommand {

    @CommandAlias("tempban")
    @CommandPermission("pixlies.moderation.tempban")
    @CommandCompletion("@players")
    @Description("Temporarily bans player with the default reason")
    public void onTempBan(CommandSender sender, OfflinePlayer target, String duration, @Optional String reason) {
        boolean silent = false;

        long durationLong = TimeUnit.getDuration(duration);

        if (durationLong == 0) {
            Lang.NOT_A_NUMBER.send(sender);
            return;
        }

        String banReason = reason;
        if (reason != null && !reason.isEmpty()) {
            banReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        final String finalReason = banReason;
        final boolean finalSilent = silent;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            User user = User.getLoadDoNotCache(target.getUniqueId());
            if (!user.isLoaded()) {
                user.load(false);
            }
            user.tempBan(finalReason, sender, durationLong, finalSilent);
        });
    }

}
