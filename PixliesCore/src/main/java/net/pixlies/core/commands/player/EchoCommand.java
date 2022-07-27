package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.command.CommandSender;

@CommandPermission("pixlies.player.echo")
public class EchoCommand extends BaseCommand {

    @CommandAlias("echo")
    @CommandCompletion("@empty")
    @Syntax("<message>")
    @Description("Echo a message")
    public void onBroadcast(CommandSender sender, String message) {
        sender.sendMessage(CC.format(message));
    }

    @Default
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
