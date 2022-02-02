package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TeleportHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("tpback|back")
public class TpBackCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final TeleportHandler tpHandler = instance.getHandlerManager().getHandler(TeleportHandler.class);

    @CommandPermission("pixlies.player.tpback")
    @Description("Returns to the last location before teleportation")
    public void onTpBack(Player sender) {
        UUID uuid = sender.getUniqueId();
        if (tpHandler.getBackLocation(uuid) == null) {
            Lang.TPBACK_NO_LOCATION.send(sender);
        } else {
            Location loc = tpHandler.getBackLocation(uuid);
            tpHandler.setBackLocation(uuid, loc);
            sender.teleport(loc);
            Lang.RETURNED_TO_PREVIOUS_LOCATION.send(sender);
        }
    }

}
