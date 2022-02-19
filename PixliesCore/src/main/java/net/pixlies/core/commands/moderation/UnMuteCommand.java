package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandAlias("unmute|mutent")
@CommandPermission("pixlies.moderation.unmute")
public class UnMuteCommand extends BaseCommand {

    @CommandCompletion("@players")
    @Description("Mute'nt a player")
    public void onUnmute(CommandSender sender, OfflinePlayer target, @Optional String s) {

        boolean silent = s != null && s.contains("-s");

        User user = User.get(target.getUniqueId());
        user.unmute(sender, silent);

    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }


}
