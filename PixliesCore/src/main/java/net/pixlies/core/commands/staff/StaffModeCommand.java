package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.impl.staffmode.StaffModeHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("staff|staffmode|sm|coolmode|h")
@CommandPermission("pixlies.moderation.staffmode")
public class StaffModeCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final StaffModeHandler handler = instance.getHandlerManager().getHandler(StaffModeHandler.class);

    @Default
    @Description("Toggle your nation bypass status")
    public void onStaffMode(Player player) {
        User user = User.get(player.getUniqueId());
        boolean staffModeEnabled = user.getSettings().isInStaffMode();
        if (staffModeEnabled) {
            handler.disable(player);
            Lang.STAFF_STAFFMODE_OFF.send(player);
        } else {
            handler.enable(player);
            Lang.STAFF_STAFFMODE_ON.send(player);
        }
    }

    @Private
    @Description("Toggle another's staff mode status")
    public void onStaffMode(CommandSender sender, Player player) {
        User user = User.get(player.getUniqueId());
        boolean staffModeEnabled = user.getSettings().isInStaffMode();
        if (staffModeEnabled) {
            handler.disable(player);
            Lang.STAFF_STAFFMODE_OFF_OTHER.send(sender, "%PLAYER%;" + player.getName());
            Lang.STAFF_STAFFMODE_OFF.send(player);
        } else {
            handler.enable(player);
            Lang.STAFF_STAFFMODE_ON_OTHER.send(sender, "%PLAYER%;" + player.getName());
            Lang.STAFF_STAFFMODE_ON.send(player);
        }
    }

}
