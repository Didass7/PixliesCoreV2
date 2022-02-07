package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("clear")
@CommandPermission("pixlies.player.clear")
public class ClearCommand extends BaseCommand {

    @Syntax("<player>")
    public void onClear(CommandSender sender, Player player) {
        if (player.getInventory().isEmpty()) {
            Lang.NO_ITEMS_FOUND_ON_PLAYER.send(sender, "%PLAYER%;" + player.getName());
            return;
        }
        player.getInventory().clear();
        Lang.PLAYER_CLEAR.send(sender, "%PLAYER%;" + player.getName());
    }

    @Default
    @Private
    public void onClear(Player player) {
        if (player.getInventory().isEmpty()) {
            Lang.NO_ITEMS_FOUND_ON_PLAYER.send(player, "%PLAYER%;" + player.getName());
            return;
        }
        player.getInventory().clear();
        Lang.PLAYER_CLEAR.send(player, "%PLAYER%;" + player.getName());
    }

}
