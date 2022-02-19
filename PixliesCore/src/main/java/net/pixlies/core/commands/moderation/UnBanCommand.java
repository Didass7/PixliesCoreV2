package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandAlias("unban|bannt")
@CommandPermission("pixlies.moderation.unban")
public class UnBanCommand extends BaseCommand {

    @CommandCompletion("@players")
    @Description("Ban'nt a player")
    public void onUnban(CommandSender sender, OfflinePlayer target, @Optional String s) {
        boolean silent = s != null && s.contains("-s");

        User user = User.get(target.getUniqueId());
        user.unban(sender, silent);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
