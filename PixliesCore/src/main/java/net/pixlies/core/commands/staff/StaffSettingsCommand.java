package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.ToggleButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
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
        ChestGui gui = new ChestGui(1, "Staff Settings");

        // Disable clicks
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // Settings pane
        StaticPane settingsPane = new StaticPane(0, 0, 5, 1);

        String enabled = "§7Status: §aEnabled";
        String disabled = "§7Status: §cDisabled";

        for (StaffSetting s : StaffSetting.values()) {
            ToggleButton button = new ToggleButton(s.ordinal(), 0, 1, 1, isSettingEnabled(user, s));
            Material material = s.getMaterial();
            ItemBuilder builder = new ItemBuilder(material)
                    .setGlow(isSettingEnabled(user, s))
                    .addLoreArray(s.getDescription());

            if (isSettingEnabled(user, s)) {
                builder.setDisplayName(ChatColor.GREEN + s.getTitle());
                builder.addLoreLine(enabled);
            } else {
                builder.setDisplayName(ChatColor.RED + s.getTitle());
                builder.addLoreLine(disabled);
            }

            GuiItem item = new GuiItem(builder.build());
            item.setAction(event -> {

                ItemBuilder b = builder.clone();

                HumanEntity opener = event.getWhoClicked();

                // GUI Shenanigans
                button.toggle();
                if (button.isEnabled()) {
                    b = builder.setDisplayName(ChatColor.GREEN + s.getTitle())
                            .removeLoreLine(disabled)
                            .addLoreLine(enabled);
                    Lang.SETTING_CHANGED.send(opener, "%SETTINGS%;" + s.getTitle(), "%STATE%;enabled");
                } else {
                    b.setDisplayName(ChatColor.RED + s.getTitle())
                            .removeLoreLine(enabled)
                            .addLoreLine(disabled);
                    Lang.SETTING_CHANGED.send(opener, "%SETTINGS%;" + s.getTitle(), "%STATE%;disabled");
                }

                ItemStack newItem = builder.build();
                settingsPane.removeItem(s.ordinal(), 0);
                settingsPane.addItem(new GuiItem(newItem), s.ordinal(), 0);

                // Actual functionality
                switch (newItem.getType()) {
                    case COMMAND_BLOCK -> user.setCommandSpyEnabled(button.isEnabled());
                    case PAPER -> user.setSocialSpyEnabled(button.isEnabled());
                    case NAME_TAG -> user.setViewBannedJoins(button.isEnabled());
                    case BARRIER -> user.setViewMutedChat(button.isEnabled());
                }
                user.save();

                gui.update();
            });
            settingsPane.addItem(item, s.ordinal(), 0);
        }

        // Add panes
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

    private static boolean isSettingEnabled(User user, StaffSetting s) {
        return switch (s) {
            case COMMANDSPY -> user.isCommandSpyEnabled();
            case SOCIALSPY -> user.isSocialSpyEnabled();
            case MUTESPY -> user.isViewMutedChat();
            case BANSPY -> user.isViewBannedJoins();
        };
    }

    @AllArgsConstructor
    public enum StaffSetting {
        COMMANDSPY("Command Spy", new String[]{"§7Spy on other players' commands"}, Material.COMMAND_BLOCK),
        SOCIALSPY("Social Spy", new String[]{"§7Spy on other players' private messages"}, Material.PAPER),
        MUTESPY("Mute Spy", new String[]{"§7Receive messages when muted", "§7players try to talk"}, Material.NAME_TAG),
        BANSPY("Ban Spy", new String[]{"§7Receive messages when banned", "§7players try to join"}, Material.BARRIER);

        @Getter private final String title;
        @Getter private final String[] description;
        @Getter private final Material material;
    }

}
