package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class BanCommand extends BaseCommand {

    @CommandAlias("ban")
    @CommandPermission("pixlies.moderation.ban")
    @CommandCompletion("@players")
    @Description("Bans player with the default reason")
    public void onBan(CommandSender sender, OfflinePlayer target, @Optional String reason) {

        boolean silent = false;

        String banReason = Main.getInstance().getConfig().getString("moderation.defaultReason", "No reason given");

        if (reason != null && !reason.isEmpty()) {
            banReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        final String finalBanReason = banReason;
        final boolean finalSilent = silent;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            User user = User.getLoadDoNotCache(target.getUniqueId());
            user.ban(finalBanReason, sender, finalSilent);
        });

    }

}
