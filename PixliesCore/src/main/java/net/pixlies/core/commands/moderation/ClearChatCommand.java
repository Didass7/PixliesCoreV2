package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("clearchat")
@CommandPermission("pixlies.moderation.clearchat")
public class ClearChatCommand extends BaseCommand {

    @Default
    @Description("Clear the chat")
    public static void onClearChat(CommandSender sender) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!p.hasPermission("pixlies.moderation.bypass.clearchat")) {
                for (int i = 0; i < 300; i++) {
                    p.sendMessage(" ");
                }
            }
        }
        Lang.CHAT_CLEARED.broadcast("%PLAYER%;" + sender.getName());
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public ClearChatCommand() {}

}
