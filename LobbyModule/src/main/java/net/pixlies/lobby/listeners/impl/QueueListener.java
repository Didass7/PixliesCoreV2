package net.pixlies.lobby.listeners.impl;

import com.google.gson.JsonObject;
import net.pixlies.core.database.redis.RedisMessageReceiveEvent;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * Check the queue on Redis receive
 * @author MickMMars
 * @author dynmie
 */
public class QueueListener implements Listener {

    private static final Lobby instance = Lobby.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    @EventHandler
    public void onReceive(RedisMessageReceiveEvent event) {
        JsonObject jsonObject = event.getData();
        switch (event.getIdentifier()) {

            // LEAVE QUEUE
            case "Queue.LeaveQueue" -> {
                String u = jsonObject.get("uuid").getAsString();
                UUID uuid = UUID.fromString(u);

                manager.getPlayerPosition().remove(uuid);
                manager.getQueueMap().remove(uuid);
            }

            // POSITION UPDATE
            case "Queue.QueuePosUpdate" -> {
                String u = jsonObject.get("uuid").getAsString();
                UUID uuid = UUID.fromString(u);

                String server = jsonObject.get("server").getAsString();
                int position = jsonObject.get("position").getAsInt();

                Player p = Bukkit.getPlayer(uuid);
                if (p == null) return;

                if (manager.isInQueue(p) && manager.getQueuePosition(p) == -1) {
                    p.sendMessage(Lang.PIXLIES + CC.format("&7An error has occurred with the queue! Please rejoin."));
                    manager.getQueueMap().remove(uuid);
                    manager.getPlayerPosition().remove(uuid);
                    return;
                }
                manager.getQueueMap().put(uuid, server);
                manager.getPlayerPosition().put(uuid, position);
            }

            case "Queue.QueueInfoUpdate" -> {
                String server = jsonObject.get("server").getAsString();
                int size = jsonObject.get("size").getAsInt();
                int limit = jsonObject.get("limit").getAsInt();
                boolean paused = jsonObject.get("paused").getAsBoolean();

                manager.getQueuePlayers().put(server, size);
                manager.getMaxPlayers().put(server, limit);

                if (!manager.getPausedQueue().containsKey(server) || manager.getPausedQueue().get(server) != paused) {
                    manager.getPausedQueue().put(server, paused);
                }
            }

        }
    }

}
