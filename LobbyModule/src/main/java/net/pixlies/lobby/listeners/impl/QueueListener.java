package net.pixlies.lobby.listeners.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.pixlies.core.database.redis.RedisMessageReceiveEvent;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import net.pixlies.lobby.managers.queue.Queue;
import net.pixlies.lobby.managers.queue.QueuePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
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

        if (!event.getIdentifier().equals("Queue.QueueUpdate")) return;
        JsonObject data = event.getData();

        JsonArray queues = data.get("queues").getAsJsonArray();
        manager.getQueues().clear();

        // FOR EACH QUEUE
        for (JsonElement rawQueue : queues) {
            JsonObject jsonQueue = rawQueue.getAsJsonObject();

            // FIELD
            String name = jsonQueue.get("name").getAsString();
            boolean paused = jsonQueue.get("paused").getAsBoolean();
            int limit = jsonQueue.get("limit").getAsInt();
            int size = jsonQueue.get("size").getAsInt();

            // FOR EACH QUEUED PLAYERS
            Map<UUID, QueuePlayer> queuedPlayers = new HashMap<>();
            for (JsonElement rawQueuedPlayer : jsonQueue.get("queuedPlayers").getAsJsonArray()) {
                JsonObject jsonQueuedPlayer = rawQueuedPlayer.getAsJsonObject();

                // FIELD
                UUID uuid = UUID.fromString(jsonQueuedPlayer.get("uuid").getAsString());
                int position = jsonQueuedPlayer.get("position").getAsInt();

                queuedPlayers.put(uuid, new QueuePlayer(uuid, position));
            }

            synchronized (manager.getQueues()) {
                manager.getQueues().put(name, new Queue(name, paused, limit, size, queuedPlayers));
            }

        }


    }

}