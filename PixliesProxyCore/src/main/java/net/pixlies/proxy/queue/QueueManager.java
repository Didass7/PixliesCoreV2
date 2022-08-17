package net.pixlies.proxy.queue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.database.redis.RedisManager;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class QueueManager {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final Config config = instance.getConfig();

    private final @Getter Map<String, Queue> queues = new HashMap<>();

    public @Nullable Queue getQueue(String name) {
        return queues.get(name.toLowerCase());
    }

    public Collection<ProxyQueuePlayer> getAllQueuedPlayers() {
        List<ProxyQueuePlayer> toReturn = new ArrayList<>();
        queues.forEach((queueName, queue) -> toReturn.addAll(queue.getQueuedPlayers()));
        return toReturn;
    }

    public Collection<UUID> getAllQueuedPlayersUuid() {
        List<UUID> toReturn = new ArrayList<>();
        queues.forEach((queueName, queue) ->
                queue.getQueuedPlayers().forEach(player ->
                        toReturn.add(player.getUuid()))
        );
        return toReturn;
    }

    public boolean isInQueue(UUID uuid) {
        return getAllQueuedPlayersUuid().contains(uuid);
    }

    public @Nullable Queue getQueueFromPlayer(UUID uuid) {
        return queues.values().stream().filter(queue -> queue.getQueuePlayer(uuid) != null).findFirst().orElse(null);
    }

    public @Nullable ProxyQueuePlayer getQueuePlayer(UUID uuid) {
        for (Queue queue : queues.values()) {
            ProxyQueuePlayer queuePlayer = queue.getQueuePlayer(uuid);
            if (queuePlayer == null) continue;
            return queuePlayer;
        }
        return null;
    }

    public Queue removePlayerFromQueue(UUID uuid) {
        if (!isInQueue(uuid)) return null;
        Queue queue = getQueueFromPlayer(uuid);
        if (queue == null) return null;
        queue.removeQueuePlayer(uuid);
        return queue;
    }

    public Queue addPlayerToQueue(String server, ProxyQueuePlayer queuePlayer) {
        Queue queue = getQueue(server);
        if (queue == null) return null;
        queue.getQueuedPlayers().add(queuePlayer);
        return queue;
    }

    public int getPosition(UUID uuid) {
        Queue queue = getQueueFromPlayer(uuid);
        if (queue == null) return -1;

        ProxyQueuePlayer queuePlayer = getQueuePlayer(uuid);
        if (queuePlayer == null) return -1;

        PriorityQueue<ProxyQueuePlayer> queuePlayers = new PriorityQueue<>(queue.getQueuedPlayers());
        if (queuePlayers.size() < 1) return 0;

        int position = 0;
        for (int i = 1; i <= queuePlayers.size(); i++) { // Starting from 1 because you can't be in position 0
            ProxyQueuePlayer indexPlayer = queuePlayers.poll();
            if (indexPlayer == null) continue;

            if (indexPlayer.getUuid() == queuePlayer.getUuid()) {
                position = i;
                break;
            }
        }

        return position;
    }

    public void requestQueueUpdate() {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, Queue> entry : queues.entrySet()) {

            String id = entry.getKey();
            Queue queue = entry.getValue();

            JsonObject jsonQueue = new JsonObject();

            // FIELDS
            jsonQueue.addProperty("name", queue.getName());
            jsonQueue.addProperty("paused", queue.isPaused());
            jsonQueue.addProperty("limit", queue.getLimit());
            jsonQueue.addProperty("size", queue.getQueuedPlayers().size());

            JsonArray jsonQueuedPlayers = new JsonArray();
            for (ProxyQueuePlayer queuePlayer : queue.getQueuedPlayers()) {
                JsonObject jsonQueuePlayer = new JsonObject();

                // FIELDS
                jsonQueuePlayer.addProperty("uuid", queuePlayer.getUuid().toString());
                jsonQueuePlayer.addProperty("position", getPosition(queuePlayer.getUuid()));

                jsonQueuedPlayers.add(jsonQueuePlayer);
            }
            jsonQueue.add("queuedPlayers", jsonQueuedPlayers);

            jsonObject.add(id, jsonQueue);

        }

        RedisManager.sendRequest("Queue.QueueUpdate", jsonObject);
    }

    public void setupQueues() {
        Configuration section = config.getConfig().getSection("queues");
        if (section == null) return;

        for (String queueName : section.getKeys()) {
            boolean paused = section.getBoolean(queueName + ".paused");
            int limit = section.getInt(queueName + ".limit");
            Queue queue = new Queue(queueName, paused, limit);
            queues.put(queueName.toLowerCase(), queue);
        }

    }

}
