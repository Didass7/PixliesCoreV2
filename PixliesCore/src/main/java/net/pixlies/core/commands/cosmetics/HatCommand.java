package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.localization.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Set an item as your hat.
 * @author dynmie
 */
@CommandPermission("pixlies.cosmetics.hat")
public class HatCommand extends BaseCommand {

    @Default
    @CommandAlias("hat|head")
    @Description("Get a cool hat!")
    public void onHat(Player player) {

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();

        if (inventory.getHelmet() != null) {
            Lang.COSMETICS_HAT_ALREADY_HAVE.send(player);
            return;
        }

        if (item.getType() == Material.AIR) {
            Lang.COSMETICS_HAT_CANNOT_SET.send(player);
            return;
        }

        inventory.setHelmet(item);
        Lang.COSMETICS_HAT_SET.send(player);

    }

}



