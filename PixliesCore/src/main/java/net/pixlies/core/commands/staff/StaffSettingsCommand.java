package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

@CommandAlias("staffsettings")
@CommandPermission("pixlies.moderation.staffsettings")
public class StaffSettingsCommand extends BaseCommand {

    @Default
    @Description("Toggle staff-only settings")
    public static void openStaffSettings(CommandSender sender) {
        Player player = (Player) sender;

        // Create GUI
        ChestGui gui = new ChestGui(3, "Staff Settings");

        // Disable clicks
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // Background
        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        // Info + close pane
        StaticPane infoAndClose = new StaticPane(4, 0, 1, 1);
        GuiItem info = new GuiItem(getInfoItem());
        infoAndClose.addItem(info, 0, 0);
        GuiItem close = new GuiItem(getCloseItem());
        close.setAction(event -> player.closeInventory());
        infoAndClose.addItem(close, 0, 2);

        // Add panes
        gui.addPane(background);
        gui.addPane(infoAndClose);

        // Show GUI
        gui.show(player);

        // create the GUI
        // show the GUI
        // make listener to check for clicks, update/toggle
        // stats as booleans!

        /* TODO

        Show "banned player tried to join"
        Show "muted player tried to talk"
        Don't clear chat for me
        Show player executed commands (commandspy)

        */
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public StaffSettingsCommand() {}

    public static ItemStack getInfoItem() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta infoMeta = item.getItemMeta();
        item.setDisplayName("§6Chat settings");
        infoMeta.setLore(Collections.singletonList("§7Modify your staff settings."));
        item.setItemMeta(infoMeta);
        return item;
    }

    public static ItemStack getCloseItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta infoMeta = item.getItemMeta();
        item.setDisplayName("§cClose");
        item.setItemMeta(infoMeta);
        return item;
    }

}
