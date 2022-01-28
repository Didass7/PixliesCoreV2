package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.ModerationHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

@CommandAlias("togglepvp|pvp")
@CommandPermission("pixlies.moderation.togglepvp")
public class TogglePvPCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final ModerationHandler moderationHandler = instance.getHandlerManager().getHandler(ModerationHandler.class);

    @Default
    @Description("Toggle PvP combat")
    public void onTogglePvP(CommandSender sender) {
        boolean pvpOn = moderationHandler.isPvpOn();
        if (pvpOn) {
            Lang.MODERATION_PVP_OFF.broadcast("%PLAYER%;" + sender.getName());
        } else {
            Lang.MODERATION_PVP_ON.broadcast("%PLAYER%;" + sender.getName());
        }
        moderationHandler.setPvpOn(!pvpOn);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public TogglePvPCommand() {}

}
