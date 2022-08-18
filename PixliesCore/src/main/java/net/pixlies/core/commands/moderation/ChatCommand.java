package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.ChatHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("chat")
@CommandPermission("pixlies.moderation.chat")
public class ChatCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final ChatHandler chatHandler = instance.getHandlerManager().getHandler(ChatHandler.class);

    @CommandAlias("mutechat")
    @Subcommand("mute")
    @Description("Toggle the chat being muted")
    public void onToggleMute(CommandSender sender) {
        boolean chatMuted = chatHandler.isMuted();
        if (chatMuted) {
            Lang.CHAT_UNMUTED.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.CHAT_MUTED.broadcast("%PLAYER%;" + sender.getName());
        }
        chatHandler.setMuted(!chatMuted);
    }

    @CommandAlias("clearchat|purgechat")
    @Subcommand("clear")
    @Description("Clear messages in chat")
    public void onClearChat(CommandSender sender) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            // bypass clearchat
            if (p.hasPermission("pixlies.moderation.chat.exempt"))
                    continue;

            for (int i = 0; i < 150; i++) {
                p.sendMessage(" ");
            }
        }
        Lang.CHAT_CLEARED.broadcast("%PLAYER%;" + sender.getName());
    }

    @CommandAlias("slowchat")
    @Subcommand("slow")
    @Description("Check or set the chat delay")
    @CommandCompletion("@empty @range:0-10")
    public void onSlow(CommandSender sender, @Optional @Conditions("limits:min=0,max=10") Integer cooldown) {
        if (cooldown == null) {
            Lang.SLOWMODE_GET.send(sender, "%VALUE%;" + chatHandler.getSlowModeDelayAsSeconds());
            return;
        }

        if (cooldown == 0) {
            chatHandler.setSlowModeDelay(0);
            Lang.SLOWMODE_OFF.send(sender, "%PLAYER%;" + sender.getName());
        } else {
            chatHandler.setSlowModeDelay(cooldown);
            Lang.SLOWMODE_SET.send(sender, "%PLAYER%;" + sender.getName(), "%VALUE%;" + cooldown);
        }

    }

    @Default
    @HelpCommand
    @Description("A command to help with moderating the chat")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
