package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("chat")
@CommandPermission("pixlies.moderation.chat")
public class ChatCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @Default
    @Description("Chat moderation")
    public static void onChat(CommandSender sender, @Optional String option, @Optional String word) {
        switch (option) {
            case "togglemute":
                boolean chatMuted = instance.isChatMuted();
                if (chatMuted) {
                    Lang.CHAT_UNMUTED.broadcast("%PLAYER%;" + sender.getName());
                } else {
                    Lang.CHAT_MUTED.broadcast("%PLAYER%;" + sender.getName());
                }
                instance.setChatMuted(!chatMuted);
                break;

            case "toggleswearing":
                boolean swearFilter = instance.isSwearFilter();
                if (swearFilter) {
                    Lang.SWEAR_FILTER_OFF.broadcast("%PLAYER%;" + sender.getName());
                } else {
                    Lang.SWEAR_FILTER_ON.broadcast("%PLAYER%;" + sender.getName());
                }
                instance.setChatMuted(!swearFilter);
                break;

            case "clear":
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!p.hasPermission("pixlies.moderation.bypass.clearchat")) {
                        for (int i = 0; i < 300; i++) {
                            p.sendMessage(" ");
                        }
                    }
                }
                Lang.CHAT_CLEARED.broadcast("%PLAYER%;" + sender.getName());
                break;

            case "wordblock":
                // TODO
                break;

            case "wordallow":
                // TODO
                break;

            default:

        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public ChatCommand() {}

}