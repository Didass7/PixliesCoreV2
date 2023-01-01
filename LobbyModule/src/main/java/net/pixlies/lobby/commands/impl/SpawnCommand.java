package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import net.pixlies.core.localization.Lang;
import net.pixlies.lobby.entity.LobbySpawn;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    @CommandAlias("spawn")
    public void onSpawn(Player player) {
        LobbySpawn lobbySpawn = LobbySpawn.getSpawn();
        player.teleport(lobbySpawn.getAsBukkitLocation());
        Lang.PLAYER_WARP.send(player, "%TARGET%;" + "Spawn");
    }

}
