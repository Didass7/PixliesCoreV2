package net.pixlies.nations.listeners.impl;

import net.pixlies.core.utils.EventUtils;
import net.pixlies.nations.commands.CommandManager;
import net.pixlies.nations.events.impl.PlayerTerritoryChangeEvent;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

public class PlayerTerritoryListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        NationProfile profile = NationProfile.get(player.getUniqueId());

        NationChunk from = NationChunk.getClaimFromChunk(event.getFrom().getChunk());
        NationChunk to = NationChunk.getClaimFromChunk(event.getTo().getChunk());

        if (from == null) from = new NationChunk("wilderness", event.getFrom().getWorld().getName(), event.getFrom().getChunk().getX(), event.getFrom().getChunk().getZ());
        if (to == null) to = new NationChunk("wilderness", event.getTo().getWorld().getName(), event.getTo().getChunk().getX(), event.getTo().getChunk().getZ());
        if (to.getNationId().equals("wilderness") && CommandManager.autoClaimPlayers.containsKey(player.getUniqueId())) {
            System.out.println("Minecraft");
            profile.attemptClaim(player, Nation.getFromId(CommandManager.autoClaimPlayers.get(player.getUniqueId())));
            return;
        }
        if (Objects.equals(from, to)) return;
        if (from.getNationId().equals(to.getNationId())) return;

        PlayerTerritoryChangeEvent playerTerritoryChangeEvent = new PlayerTerritoryChangeEvent(player, from, to);
        EventUtils.call(playerTerritoryChangeEvent);

        event.setCancelled(playerTerritoryChangeEvent.isCancelled());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        NationChunk from = NationChunk.getClaimFromChunk(event.getFrom().getChunk());
        NationChunk to = NationChunk.getClaimFromChunk(event.getTo().getChunk());

        if (Objects.equals(from, to)) return;

        if (from == null) from = new NationChunk("wilderness", event.getFrom().getWorld().getName(), event.getFrom().getChunk().getX(), event.getFrom().getChunk().getZ());
        if (to == null) to = new NationChunk("wilderness", event.getTo().getWorld().getName(), event.getTo().getChunk().getX(), event.getTo().getChunk().getZ());
        if (from.getNationId().equals(to.getNationId())) return;

        PlayerTerritoryChangeEvent playerTerritoryChangeEvent = new PlayerTerritoryChangeEvent(player, from, to);
        EventUtils.call(playerTerritoryChangeEvent);

        event.setCancelled(playerTerritoryChangeEvent.isCancelled());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        NationChunk to = NationChunk.getClaimFromChunk(event.getRespawnLocation().getChunk());

        if (to == null) to = new NationChunk("wilderness", event.getRespawnLocation().getWorld().getName(), event.getRespawnLocation().getChunk().getX(), event.getRespawnLocation().getChunk().getZ());

        PlayerTerritoryChangeEvent playerTerritoryChangeEvent = new PlayerTerritoryChangeEvent(player, null, to);
        EventUtils.call(playerTerritoryChangeEvent);
    }

}
