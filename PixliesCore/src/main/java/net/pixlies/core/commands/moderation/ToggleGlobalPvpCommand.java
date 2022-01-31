package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.GlobalPvpHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

public class ToggleGlobalPvpCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final GlobalPvpHandler globalPvpHandler = instance.getHandlerManager().getHandler(GlobalPvpHandler.class);

    @CommandAlias("toggleglobalpvp")
    @CommandPermission("pixlies.moderation.globalpvp")
    @Description("Toggle Global PvP Combat")
    public void onTogglePvP(CommandSender sender) {
        boolean pvpEnabled = globalPvpHandler.isGlobalPvpEnabled();
        if (pvpEnabled) {
            Lang.MODERATION_GLOBAL_PVP_OFF.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.MODERATION_GLOBAL_PVP_ON.broadcast("%PLAYER%;" + sender.getName());
        }
        globalPvpHandler.setGlobalPvpEnabled(!pvpEnabled);
    }

}
