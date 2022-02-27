package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.GlobalPvpHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

@CommandAlias("globalpvp")
@CommandPermission("pixlies.moderation.globalpvp")
public class GlobalPvpCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final GlobalPvpHandler globalPvpHandler = instance.getHandlerManager().getHandler(GlobalPvpHandler.class);

    @Default
    @Description("Toggle Global PvP Combat")
    public void onTogglePvP(CommandSender sender) {
        boolean pvpEnabled = globalPvpHandler.isGlobalPvpEnabled();
        if (pvpEnabled) {
            Lang.MODERATION_GLOBAL_PVP_OFF.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.MODERATION_GLOBAL_PVP_ON.broadcast("%PLAYER%;" + sender.getName());
        }
        globalPvpHandler.setGlobalPvp(!pvpEnabled);
    }

}
