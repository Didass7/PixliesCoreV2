package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.FreezeHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand extends BaseCommand {

    private final FreezeHandler handler = Main.getInstance().getHandlerManager().getHandler(FreezeHandler.class);

    @CommandAlias("freeze|ss")
    @CommandPermission("pixlies.moderation.freeze")
    public void onFreeze(CommandSender sender, Player target) {
        if (handler.isFrozen(target.getUniqueId())) {
            handler.unfreeze(target);
            Lang.MODERATION_FREEZE_OFF.send(sender, "%PLAYER%;" + target.getName());
        } else {
            if (target.hasPermission("pixlies.moderation.freeze.exempt")) {
                Lang.MODERATION_CANNOT_FREEZE.send(sender);
                return;
            }
            handler.freeze(target);
            Lang.MODERATION_FREEZE_ON.send(sender, "%PLAYER%;" + target.getName());
            Lang.MODERATION_FREEZE_MESSAGE.broadcastPermission("pixlies.moderation.silent",
                    "%PLAYER%;" + target.getName(),
                    "%EXECUTOR%;" + sender.getName());
        }
    }

}
