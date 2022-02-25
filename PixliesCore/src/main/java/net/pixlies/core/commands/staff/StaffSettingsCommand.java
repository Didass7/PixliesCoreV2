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
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Customize the staff experience.
 * @author vPrototype_
 */
@CommandAlias("staffsettings")
@CommandPermission("pixlies.staff.staffsettings")
public class StaffSettingsCommand extends BaseCommand {

    @Default
    @Description("Open staff settings menu")
    public static void openStaffSettings(Player player) {
        User user = User.get(player.getUniqueId());

        // Create GUI
        ChestGui gui = new ChestGui(3, "Staff settings");

        // Disable clicks
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // Background
        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        // Toggle text
        String on = "§7Toggled: §aON";
        String off = "§7Toggled: §cOFF";

        // Settings pane
        StaticPane settingsPane = new StaticPane(2, 1, 5, 1);
        for (StaffSetting s : StaffSetting.values()) {
            ToggleButton button = new ToggleButton(s.ordinal(), 0, 1, 1, user.getPersonalization().isSettingEnabled(s));
            Material material = s.getMaterial();
            ItemBuilder builder = new ItemBuilder(material)
                    .setDisplayName(s.getTitle())
                    .setGlow(user.getPersonalization().isSettingEnabled(s))
                    .addLoreArray(s.getDescription())
                    .addLoreLine(" ");
            if (user.getPersonalization().isSettingEnabled(s)) builder.addLoreLine(on);
            else builder.addLoreLine(off);

            GuiItem item = new GuiItem(builder.build());
            item.setAction(event -> {
                // GUI Shenanigans
                button.toggle();
                builder.setGlow(button.isEnabled());
                player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
                if (button.isEnabled()) {
                    builder.removeLoreLine(off);
                    builder.addLoreLine(on);
                    Lang.STAFF_SETTING_CHANGED.send(player, "%SETTING%;" + s.getTitle().toLowerCase(),
                            "%STATE%;" + "§aon");
                } else {
                    builder.removeLoreLine(on);
                    builder.addLoreLine(off);
                    Lang.STAFF_SETTING_CHANGED.send(player, "%SETTING%;" + s.getTitle().toLowerCase(),
                            "%STATE%;" + "§coff");
                }

                ItemStack newItem = builder.build();
                settingsPane.removeItem(s.ordinal(), 0);
                settingsPane.addItem(new GuiItem(newItem), s.ordinal(), 0);

                // Actual functionality
                switch (newItem.getDisplayName()) {
                    case "§9Command spy" -> user.getPersonalization().setCommandSpyEnabled(button.isEnabled());
                    case "§6Social spy" -> user.getPersonalization().setSocialSpyEnabled(button.isEnabled());
                    case "§cBan spy" -> user.getPersonalization().setViewBannedJoins(button.isEnabled());
                    case "§cMute spy" -> user.getPersonalization().setViewMutedChat(button.isEnabled());
                    case "§3Bypass clearchat" -> user.getPersonalization().setBypassClearChat(button.isEnabled());
                }
                user.save();

                gui.update();
            });
            settingsPane.addItem(item, s.ordinal(), 0);
        }

        // Add panes
        gui.addPane(background);
        gui.addPane(settingsPane);

        // Show GUI
        gui.show(player);
        gui.update();
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public StaffSettingsCommand() {}

    @AllArgsConstructor
    public enum StaffSetting {
        COMMANDSPY("§9Command spy", new String[]{"§7Spy on other players' commands"}, Material.COMMAND_BLOCK),
        SOCIALSPY("§6Social spy", new String[]{"§7Spy on other players' private messages"}, Material.PAPER),
        MUTESPY("§cMute spy", new String[]{"§7Receive messages when muted", "§7players try to talk"}, Material.NAME_TAG),
        BANSPY("§cBan spy", new String[]{"§7Receive messages when banned", "§7players try to join"}, Material.BARRIER),
        BYPASSCLEARCHAT("§3Bypass clearchat", new String[]{"§7Bypass the chat getting cleared"}, Material.MAP);

        @Getter private final String title;
        @Getter private final String[] description;
        @Getter private final Material material;
    }

}
