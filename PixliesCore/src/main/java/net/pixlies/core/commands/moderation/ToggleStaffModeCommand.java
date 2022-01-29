package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.StaffModeHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

@CommandAlias("togglestaffmode")
@CommandPermission("pixlies.moderation.staffmode")
public class ToggleStaffModeCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final StaffModeHandler staffModeHandler = instance.getHandlerManager().getHandler(StaffModeHandler.class);

    @Default
    @Description("Toggles if only staff can join")
    public void onStaffMode(CommandSender sender) {
        boolean staffModeEnabled = staffModeHandler.isStaffModeEnabled();
        if (staffModeEnabled) {
            Lang.STAFFMODE_OFF.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.STAFFMODE_ON.broadcast("%PLAYER%;" + sender.getName());
        }
        staffModeHandler.setStaffModeEnabled(!staffModeEnabled);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
