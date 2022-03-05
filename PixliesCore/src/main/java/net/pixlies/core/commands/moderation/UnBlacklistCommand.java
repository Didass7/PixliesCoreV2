package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * Unblacklist a player, created because i blacklisted myself
 * @author Dynmie
 */
public class UnBlacklistCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @CommandAlias("unblacklist|blacklistnt")
    @CommandPermission("pixlies.moderation.unblacklist")
    @CommandCompletion("@players")
    @Description("Unblacklist a player")
    public void onUnBlacklist(CommandSender sender, OfflinePlayer target, @Optional String s) {

        boolean silent = s != null && s.contains("-s");
        User user = User.get(target.getUniqueId());

        if (!user.isBlacklisted()) {
            Lang.PLAYER_NOT_BLACKLISTED.send(sender);
            return;
        }

        user.unblacklist(sender, silent);

    }

}
