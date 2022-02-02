package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("feed")
@CommandPermission("pixlies.cosmetics.feed")
public class FeedCommand extends BaseCommand {

    @Default
    public void onFeed(Player player) {
        player.setFoodLevel(20);
        Lang.COSMETICS_FEED_SELF.send(player);
    }

    @Private
    @CommandPermission("pixlies.cosmetics.feed.others")
    @CommandCompletion("@players")
    public void onFeed(CommandSender sender, Player target) {
        target.setFoodLevel(20);
        Lang.COSMETICS_FEED_OTHERS.send(sender, "%PLAYER%;" + target.getName());
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
