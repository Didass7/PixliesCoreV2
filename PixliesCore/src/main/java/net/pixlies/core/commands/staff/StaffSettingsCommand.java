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

        // Settings pane
        StaticPane settings = new StaticPane(1, 1, 7, 1);

        /* TODO
           in the settings pane, we need 4 buttons:
             - Banspy (show "banned player tried to join")
             - Mutespy (show "muted player tried to talk")
             - Commandspy (show player executed commands)
             - Bypass clear chat
           eventually, we'll implement them as actual settings
           as of right now we need the buttons
         */

        // Add panes
        gui.addPane(background);
        gui.addPane(infoAndClose);
        gui.addPane(settings);

        // Show GUI
        gui.show(player);
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
