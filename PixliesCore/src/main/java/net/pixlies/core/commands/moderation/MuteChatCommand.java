package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

@CommandAlias("mutechat")
@CommandPermission("pixlies.moderation.mutechat")
public class MuteChatCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @Default
    @Description("Toggle muting all players")
    public static void onMuteChat(CommandSender sender) {
        boolean value = instance.isChatMuted();
        if (value) {
            Lang.CHAT_UNMUTED.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.CHAT_MUTED.broadcast("%PLAYER%;" + sender.getName());
        }
        instance.setChatMuted(!value);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public MuteChatCommand() {}

}

