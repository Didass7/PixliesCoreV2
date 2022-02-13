package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
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
    public void onBlacklist(CommandSender sender, OfflinePlayer target, @Optional String reason) {

        boolean silent = false;
        String blacklistReason = instance.getConfig().getString("moderation.defaultReason", "No reason given");

        if (reason != null && !reason.isEmpty()) {
            blacklistReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        User user = User.get(target.getUniqueId());
        user.blacklist(blacklistReason, sender, silent);

    }

}
