package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @CommandAlias("staff|staffmode|h")
    @CommandPermission("pixlies.moderation.staffmode")
    @Description("Toggles staff mode")
    public void onStaffMode(CommandSender sender) {
        Player player = (Player) sender;
        User user = User.get(player.getUniqueId());
        boolean staffModeEnabled = user.isStaffModeEnabled();
        if (staffModeEnabled) {
            Lang.STAFFMODE_ON.send(sender);
        } else {
            Lang.STAFFMODE_OFF.send(sender);
        }
        user.setStaffModeEnabled(!staffModeEnabled);

        // TODO other stuff
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
