package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import net.pixlies.core.entity.Warp;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    @CommandAlias("spawn")
    @CommandPermission("pixlies.player.spawn")
    public void onSpawn(Player player) {
        User user = User.get(player.getUniqueId());

        if (user.isInCombat()) {
            Lang.COMBAT_TELEPORT.send(player);
            return;
        }

        if (user.isTeleporting()) {
            user.cancelTeleport();
            Lang.TP_CANCEL.send(player);
            return;
        }

        user.teleportToSpawn();
        Warp spawn = Warp.getSpawn();
        Lang.PLAYER_WARP.send(player, "%TARGET%;" + spawn.getName());
    }

}