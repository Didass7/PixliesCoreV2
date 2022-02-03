package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.impl.staffmode.StaffModeHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("staff|staffmode|h")
@CommandPermission("pixlies.moderation.staffmode")
public class StaffModeCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final StaffModeHandler handler = instance.getHandlerManager().getHandler(StaffModeHandler.class);

    @Default
    @Description("Toggle your staff mode status")
    public void onStaffMode(Player player) {
        User user = User.get(player.getUniqueId());
        boolean staffModeEnabled = user.getSettings().isStaffModeEnabled();
        if (staffModeEnabled) {
            handler.enable(player);
            Lang.STAFFMODE_ON.send(player);
        } else {
            handler.disable(player);
            Lang.STAFFMODE_OFF.send(player);
        }
    }

}
