package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TeleportHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("tpask|tpa")
@CommandPermission("pixlies.player.tpask")
public class TpAskCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final TeleportHandler tpHandler = instance.getHandlerManager().getHandler(TeleportHandler.class);

    @CommandCompletion("@players")
    @Description("Requests for the sender to teleport to the target")
    public void onTpAsk(Player sender, Player target) {
        tpHandler.setTpAskPlayer(target.getUniqueId(), sender.getUniqueId());
        Lang.TPASK_MESSAGE_SENDER.send(sender, "%PLAYER%;" + target.getName());
        Lang.TPASK_MESSAGE_TARGET.send(target, "%PLAYER%;" + sender.getName());
    }

}
