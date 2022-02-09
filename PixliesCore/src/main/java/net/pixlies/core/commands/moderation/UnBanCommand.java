package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandAlias("unban|bannt")
@CommandPermission("pixlies.moderation.unban")
public class UnBanCommand extends BaseCommand {

    @CommandCompletion("@players")
    @Description("Ban'nt a player")
    public void onUnmute(CommandSender sender, String target, @Optional String silent) {
        OfflinePlayer targetOP = Bukkit.getOfflinePlayerIfCached(target);
        if (targetOP == null) {
            Lang.PLAYER_DOESNT_EXIST.send(sender);
            return;
        }

        User user = User.get(targetOP.getUniqueId());
        user.unban(sender, silent != null && silent.equalsIgnoreCase("-s"));
    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
