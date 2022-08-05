package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.Warp;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * Simple warp command
 * @author Dynmie
 */
public class WarpCommand extends BaseCommand {

    @CommandAlias("warp|warps")
    @CommandPermission("pixlies.player.warp")
    @CommandCompletion("@empty")
    @Syntax("<warp> <players>")
    public void onWarp(Player player, @Optional Warp warp, @Optional Player target) {
        if (warp == null) {
            User user = User.get(player.getUniqueId());
            if (user.isTeleporting()) {
                Lang.TP_CANCEL.send(player);
                return;
            }

            // TODO: some sort of gui system to view all available warps and some sort of economy system
            // here's a placeholder, remove this later
            Collection<Warp> warps = Warp.getWarps();
            StringJoiner joiner = new StringJoiner("§8, ");
            for (Warp w : warps) {
                joiner.add("§f" + w.getName());
            }
            player.sendMessage("§7Warps [§b" + warps.size() + "§7]: " + joiner);

        } else {

            // PLAYER
            if (player.getUniqueId().equals(target.getUniqueId())) {
                User user = User.get(player.getUniqueId());
                if (user.isInCombat()) {
                    Lang.COMBAT_TELEPORT.send(player);
                    return;
                }
                if (user.isTeleporting()) {
                    Lang.TP_CANCEL.send(player);
                    return;
                }
                user.teleport(warp);
                return;
            }

            // TARGET
            if (player.hasPermission("pixlies.player.warp.other") || player.hasPermission("pixlies.staff.tp")) {
                target.teleport(warp.getAsBukkitLocation());
                Lang.PLAYER_WARP_OTHER.send(player, "%PLAYER%;" + target.getName(), "%TARGET%;" + warp.getName());
            }

        }
    }

}
