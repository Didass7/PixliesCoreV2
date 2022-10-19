package net.pixlies.nations.listeners.impl;

import net.pixlies.nations.nations.chunk.NationChunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class PlayerTerritoryListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        NationChunk from = NationChunk.getClaimFromChunk(event.getFrom().getChunk());
        NationChunk to = NationChunk.getClaimFromChunk(event.getTo().getChunk());

        if (Objects.equals(from, to)) return;

        // TODO: Territory Change event call & handle
//        PlayerTerritoryChangeEvent playerTerritoryChangeEvent = new PlayerTerritoryChangeEvent(player, from, to);
//        EventUtils.call(playerTerritoryChangeEvent);

//        event.setCancelled(playerTerritoryChangeEvent.isCancelled());
    }

}
