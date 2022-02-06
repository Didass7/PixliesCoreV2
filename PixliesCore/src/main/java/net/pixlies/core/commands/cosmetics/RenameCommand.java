package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;

import net.kyori.adventure.text.Component;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("rename")
@CommandPermission("pixlies.cosmetics.rename")
public class RenameCommand extends BaseCommand {

    @Private
    @Default
    @Description("Renames items")
    @Syntax("<name>")
    public void onRepair(Player player, String message) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            Lang.CANNOT_EDIT_ITEM.send(player);
            return;
        }
        String name = player.hasPermission("pixlies.cosmetics.rename.color") ? CC.format(message) : message;
        meta.displayName(Component.text(name));
        item.setItemMeta(meta);
        Lang.COSMETICS_RENAME_ITEM.send(player, "%MESSAGE%;" + name);
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
