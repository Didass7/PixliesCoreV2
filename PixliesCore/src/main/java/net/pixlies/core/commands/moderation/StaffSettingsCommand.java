package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

        gui.addPane(background);

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

}
