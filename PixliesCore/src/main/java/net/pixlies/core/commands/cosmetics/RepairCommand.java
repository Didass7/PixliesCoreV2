package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import lombok.val;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@CommandAlias("repair")
@CommandPermission("pixlies.cosmetics.repair")
public class RepairCommand extends BaseCommand {

    @Private
    @Default
    @Description("Repairs items")
    public void onRepair(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!(item.getItemMeta() instanceof Damageable meta)) {
            Lang.COSMETICS_CANNOT_REPAIR_ITEM.send(player);
            return;
        }
        meta.setDamage(item.getType().getMaxDurability());
        item.setItemMeta(meta);
        Lang.COSMETICS_REPAIR_ITEM.send(player);
    }

    @Subcommand("all|everything|*")
    @Description("Repairs items")
    @CommandPermission("pixlies.cosmetics.repair.all")
    public void onRepairEverything(Player player) {
        val toRepair = new ArrayList<ItemStack>();
        for (ItemStack item : player.getInventory()) {
            if (!(item.getItemMeta() instanceof Damageable)) continue;
            toRepair.add(item);
        }
        if (toRepair.isEmpty()) {
            Lang.COSMETICS_CANNOT_REPAIR_ITEM.send(player);
            return;
        }
        toRepair.forEach(item -> {
            Damageable meta = (Damageable) item.getItemMeta();
            meta.setDamage(item.getType().getMaxDurability());
            item.setItemMeta(meta);
        });
        Lang.COSMETICS_REPAIR_ITEM.send(player);
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
