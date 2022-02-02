package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("staff|staffmode|h")
@CommandPermission("pixlies.moderation.staffmode")
public class StaffModeCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @Default
    @Description("Toggles staff mode")
    public void onStaffMode(Player sender) {
        User user = User.get(sender.getUniqueId());
        boolean staffModeEnabled = user.isStaffModeEnabled();
        if (staffModeEnabled) {
            Lang.STAFFMODE_ON.send(sender);
        } else {
            Lang.STAFFMODE_OFF.send(sender);
        }
        user.setStaffModeEnabled(!staffModeEnabled);

        // TODO other stuff
    }

}
