package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.Warp;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Simple warp command
 * @author Dynmie
 */
public class WarpCommand extends BaseCommand {

    @CommandAlias("warp")
    @CommandPermission("pixlies.player.warp")
    @CommandCompletion("@empty")
    public void onWarp(Player player, @Optional Warp warp, @Optional Player target) {
        if (warp == null) {
            // TODO: some sort of gui system to view all available warps and some sort of token system
            // here's a placeholder, remove this later
            Warp.getWarps().forEach(w -> player.sendMessage(ChatColor.AQUA + w.getName() + " " + w.getDescription()));

        } else {

            // PLAYER
            if (target == null) {
                User user = User.get(player.getUniqueId());
                user.teleport(warp);
                return;
            }

            // TARGET
            if (player.hasPermission("pixlies.player.warp.other") || player.hasPermission("pixlies.staff.tp")) {
                User user = User.get(target.getUniqueId());
                user.teleport(warp);
                Lang.PLAYER_WARP_OTHER.send(player, "%PLAYER%;" + target.getName(), "%TARGET%;" + warp.getName());
            }

        }
    }

}
