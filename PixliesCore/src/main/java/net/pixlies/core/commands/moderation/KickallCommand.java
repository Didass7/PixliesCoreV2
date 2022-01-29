package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("kickall")
@CommandPermission("pixlies.moderation.kickall")
public class KickallCommand extends BaseCommand {

    @Default
    @Description("Kicks all nonstaff players")
    public void onBan(CommandSender sender, @Optional String reason) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != sender && !p.hasPermission("pixlies.moderation.kickall.bypass")) {
                if (reason == null) {
                    Lang.KICKALL_MESSAGE.kickPlayer(sender, "%PLAYER%;" + sender.getName());
                } else {
                    Lang.KICKALL_MESSAGE.kickPlayer(sender, "%PLAYER%;" + sender.getName(), "%REASON%;" + reason);
                }
            }
        }
        if (reason == null) {
            Lang.ISSUED_KICKALL.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.ISSUED_KICKALL.broadcast("%PLAYER%;" + sender.getName(), "%REASON%;" + reason);
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
