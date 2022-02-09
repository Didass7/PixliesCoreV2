package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.ToggleButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author vPrototype_
 */
@CommandAlias("staffsettings")
@CommandPermission("pixlies.staff.staffsettings")
public class StaffModeSettingsCommand extends BaseCommand {

    /*

    TODO
    - lang messages
    - toggles???

     */

    @Default
    @Description("Toggle staffmode settings")
    public static void openStaffModeSettings(Player player) {
        // Create GUI
        ChestGui gui = new ChestGui(3, "Staff Settings");

        // Disable clicks
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // Background
        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        // Info pane
        /*
        StaticPane infoPane = new StaticPane(4, 0, 1, 1);
        ItemBuilder infoBuilder = new ItemBuilder(Material.ENCHANTED_BOOK)
                .setDisplayName("§6Chat settings")
                .addLoreLine("§7Modify your staff settings");
        infoPane.addItem(new GuiItem(infoBuilder.build()), 0, 0);
         */

        // Toggle text
        String on = "§7Toggled: §aON";
        String off = "§7Toggled: §cOFF";

        // Settings pane
        StaticPane settingsPane = new StaticPane(2, 1, 5, 1);
        for (Settings s : Settings.values()) {
            ToggleButton button = new ToggleButton(s.ordinal(), 0, 1, 1, s.isEnabledByDefault());
            Material material = s.getMaterial();
            ItemBuilder builder = new ItemBuilder(material)
                    .setDisplayName(s.getTitle())
                    .setGlow(s.isEnabledByDefault())
                    .addLoreArray(s.getDescription())
                    .addLoreLine(" ");
            if (s.isEnabledByDefault()) {
                builder.addLoreLine(on);
            } else {
                builder.addLoreLine(off);
            }
            GuiItem item = new GuiItem(builder.build());
            item.setAction(event -> {
                button.toggle();
                builder.setGlow(button.isEnabled());
                player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
                if (button.isEnabled()) {
                    builder.removeLoreLine(off);
                    builder.addLoreLine(on);
                    Lang.STAFF_STAFFMODE_SETTING_ON.send(player, "%SETTING%;" + s.getTitle().toLowerCase());
                } else {
                    builder.removeLoreLine(on);
                    builder.addLoreLine(off);
                    Lang.STAFF_STAFFMODE_SETTING_OFF.send(player, "%SETTING%;" + s.getTitle().toLowerCase());
                }
                settingsPane.addItem(item, s.ordinal(), 0);
                gui.update();
                gui.show(player);
            });
            settingsPane.addItem(item, s.ordinal(), 0);
        }

        // Add panes
        gui.addPane(background);
        // gui.addPane(infoPane);
        gui.addPane(settingsPane);

        // Show GUI
        gui.show(player);
        gui.update();
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public StaffModeSettingsCommand() {}

    @AllArgsConstructor
    public enum Settings {
        COMMANDSPY("§9Command spy", new String[]{"§7Spy on other players' commands"}, Material.COMMAND_BLOCK, true),
        SOCIALSPY("§6Social spy", new String[]{"§7Spy on other players' private messages"}, Material.PAPER, true),
        BANSPY("§cBan spy", new String[]{"§7Receive messages when banned", "§7players try to join"}, Material.BARRIER, true),
        MUTESPY("§cMute spy", new String[]{"§7Receive messages when muted", "§7players try to talk"}, Material.NAME_TAG, true),
        BYPASSCLEARCHAT("§3Bypass clearchat", new String[]{"§7Bypass the chat getting cleared"}, Material.MAP, false);

        @Getter private final String title;
        @Getter private final String[] description;
        @Getter private final Material material;
        @Getter private final boolean enabledByDefault;
    }

}
