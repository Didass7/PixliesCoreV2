package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

public class DiscordCommand extends BaseCommand {

    private final Config config = Main.getInstance().getConfig();

    @CommandAlias("discord|disc")
    public void onDiscord(CommandSender sender) {
        Lang.PLAYER_DISCORD_MESSAGE.send(sender, "%MESSAGE%;" + config.getString("links.discord", " "));
    }

}
