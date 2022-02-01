package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TeleportHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAcceptCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final TeleportHandler tpHandler = instance.getHandlerManager().getHandler(TeleportHandler.class);

    @CommandAlias("tpaccept")
    @CommandPermission("pixlies.player.tpaccept")
    @CommandCompletion("@players")
    @Description("Accepts teleport request")
    public void onTpAccept(CommandSender sender) {
        Player player = (Player) sender;

        // If nobody has requested teleportation
        if (tpHandler.getTpAskPlayer(player.getUniqueId()) == null) {
            Lang.TPACCEPT_NOBODY.send(sender);
        } else {
            Player target = (Player) Bukkit.getOfflinePlayer(tpHandler.getTpAskPlayer(player.getUniqueId()));

            // If target is no longer online
            if (!target.isOnline()) {
                Lang.TPACCEPT_PLAYER_NOT_ONLINE.send(sender);
            } else {
                tpHandler.setBackLocation(target.getUniqueId(), target.getLocation());
                target.teleport(player);
                Lang.TPASK_ACCEPTED_MESSAGE_SENDER.send(sender, "%PLAYER%;" + target.getName());
                Lang.TPASK_ACCEPTED_MESSAGE_TARGET.send(target, "%PLAYER%;" + sender.getName());
            }
        }
    }

}
