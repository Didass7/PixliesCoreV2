package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
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

        User user = User.get(target.getUniqueId());
        user.ban(banReason, sender, silent);

    }

}
