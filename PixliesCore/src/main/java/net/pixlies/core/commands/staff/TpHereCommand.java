package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TeleportHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("tphere")
@CommandPermission("pixlies.staff.tphere")
public class TpHereCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final TeleportHandler tpHandler = instance.getHandlerManager().getHandler(TeleportHandler.class);

    @CommandCompletion("@players")
    @Description("Teleports target player to sender")
    public void onTpHere(Player sender, Player target) {
        tpHandler.setBackLocation(target.getUniqueId(), target.getLocation());
        target.teleport(sender);
        Lang.TPHERE_MESSAGE_SENDER.send(sender, "%PLAYER%;" + target.getName());
        Lang.TPHERE_MESSAGE_TARGET.send(target, "%PLAYER%;" + sender.getName());
    }

}
