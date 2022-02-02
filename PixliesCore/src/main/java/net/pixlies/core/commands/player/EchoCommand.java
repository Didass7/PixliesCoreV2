package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.pixlies.core.utils.CC;
import org.bukkit.command.CommandSender;

@CommandPermission("pixlies.player.echo")
public class EchoCommand extends BaseCommand {

    @Default
    @CommandAlias("echo")
    public void onEcho(CommandSender sender, String text) {
        sender.sendMessage(CC.format(text));
    }

}
