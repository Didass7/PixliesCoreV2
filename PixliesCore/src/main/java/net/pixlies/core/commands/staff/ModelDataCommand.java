package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("modeldata")
@CommandPermission("pixlies.staff.modeldata")
public class ModelDataCommand extends BaseCommand {

    @Subcommand("set")
    @CommandCompletion("@empty")
    public void onItemData(Player player, Integer integer) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            Lang.NOT_HOLDING_ANYTHING.send(player);
            return;
        }
        item.setCustomModelData(integer);
        Lang.STAFF_MODELDATA_SET.send(player, "%DATA%;" + integer);
    }

    @Default
    @CommandCompletion("@empty")
    public void onItemData(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            Lang.NOT_HOLDING_ANYTHING.send(player);
            return;
        }
        String data = item.hasCustomModelData() ? String.valueOf(item.getCustomModelData()) : "empty";
        Lang.STAFF_MODELDATA_GET.send(player, "%DATA%;" + data);
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
