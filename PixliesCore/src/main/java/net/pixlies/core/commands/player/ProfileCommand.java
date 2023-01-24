package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import org.bukkit.command.CommandSender;

public class ProfileCommand extends BaseCommand {

    private final Config config = Main.getInstance().getConfig();

    @CommandAlias("profile|settings")
    public void onProfile(CommandSender sender) {

    }

}
