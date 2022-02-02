package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;

@CommandAlias("enderchest|ec")
@CommandPermission("pixlies.cosmetics.enderchest")
public class EnderChestCommand extends BaseCommand {

    @Default
    public void onEnderChest(Player player) {
        player.openInventory(player.getEnderChest());
    }

    @Private
    @CommandPermission("pixlies.cosmetics.enderchest.others")
    @CommandCompletion("@players")
    public void onEnderChest(Player player, Player target) {
        player.openInventory(target.getEnderChest());
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
