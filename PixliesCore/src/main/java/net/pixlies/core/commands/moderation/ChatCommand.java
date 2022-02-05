package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.impl.ChatHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

    @Subcommand("filter")
    @Description("Toggle the chat filter")
    public void onToggleSwear(CommandSender sender) {
        boolean swearFilter = chatHandler.isSwearFilterEnabled();
        if (swearFilter) {
            Lang.SWEAR_FILTER_OFF.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.SWEAR_FILTER_ON.broadcast("%PLAYER%;" + sender.getName());
        }
        chatHandler.setSwearFilterEnabled(!swearFilter);
    }

    @CommandAlias("clearchat")
    @Subcommand("clear")
    @Description("Clear messages in chat")
    public void onClearChat(CommandSender sender) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("pixlies.moderation.chat.clear.exempt")) continue;
            for (int i = 0; i < 100; i++) {
                p.sendMessage(" ");
            }
        }
        Lang.CHAT_CLEARED.broadcast("%PLAYER%;" + sender.getName());
    }

    @Subcommand("toggleword")
    @Description("Toggle the usage of a certain word in chat")
    public void onToggleWord(CommandSender sender, String word) {
        if (word == null) {
            sender.sendMessage(Lang.PIXLIES + "You have to enter the word you want to block/unblock.");
            return;
        }

        List<String> blockedWords = new ArrayList<>(instance.getConfig().getStringList("blockedWords"));
        String lcWord = word.toLowerCase();

        if (blockedWords.contains(lcWord)) {
            blockedWords.remove(lcWord);
            instance.getConfig().set("blockedWords", blockedWords);
            Lang.REMOVED_BLOCKED_WORD.send(sender);
        } else {
            blockedWords.add(lcWord);
            instance.getConfig().set("blockedWords", blockedWords);
            Lang.ADDED_BLOCKED_WORD.send(sender);
        }

        instance.getConfig().save();
        instance.getConfig().reload();
    }

    @Subcommand("slow")
    @Description("Slows the chat down")
    @CommandCompletion("@range:0-10")
    public void onSlow(CommandSender sender, @Conditions("longLimits:min=0,max=10") Long cooldown) {
        if (cooldown == 0) {
            chatHandler.setSlowMode(0);
            Lang.SLOWMODE_OFF.send(sender, "%PLAYER%;" + sender.getName());
        } else {
            chatHandler.setSlowMode(cooldown);
            Lang.SLOWMODE_SET.send(sender, "%PLAYER%" + sender.getName(), "%VALUE%;" + cooldown);
        }
    }

    @Default
    @HelpCommand
    @Description("Chat moderation")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
