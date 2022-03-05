package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.ChatHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    @CommandAlias("clearchat")
    @Subcommand("clear")
    @Description("Clear messages in chat")
    public void onClearChat(CommandSender sender) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            // Staff setting: bypass clearchat
            if (sender instanceof Player player) {
                User user = User.get(player.getUniqueId());
                if (user.getPersonalization().isBypassClearChat()) continue;
            }

            for (int i = 0; i < 150; i++) {
                p.sendMessage(" ");
            }
        }
        Lang.CHAT_CLEARED.broadcast("%PLAYER%;" + sender.getName());
    }

    @Subcommand("toggleword")
    @Description("Toggle the usage of a certain word in chat")
    public void onToggleWord(CommandSender sender, String word) {
        if (chatHandler.isBlocked(word)) {
            chatHandler.unblockWord(word);
            Lang.REMOVED_BLOCKED_WORD.send(sender);
        } else {
            chatHandler.blockWord(word);
            Lang.ADDED_BLOCKED_WORD.send(sender);
        }
    }

    @Subcommand("slow")
    @Description("Slows the chat down")
    @CommandCompletion("@range:0-10")
    public void onSlow(CommandSender sender, @Conditions("longLimits:min=0,max=10") long cooldown) {
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
