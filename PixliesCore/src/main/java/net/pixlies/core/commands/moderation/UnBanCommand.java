package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class UnBanCommand extends BaseCommand {

    @CommandAlias("unban|bannt")
    @CommandPermission("pixlies.moderation.unban")
    @CommandCompletion("@players")
    @Description("Ban'nt a player")
    public void onUnban(CommandSender sender, OfflinePlayer target, @Optional String s) {
        boolean silent = s != null && s.contains("-s");

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            User user = User.getLoadDoNotCache(target.getUniqueId());
            if (!user.isBanned()) {
                Lang.PLAYER_NOT_BANNED.send(sender);
                return;
            }
            user.unban(sender, silent);
        });

    }

}
