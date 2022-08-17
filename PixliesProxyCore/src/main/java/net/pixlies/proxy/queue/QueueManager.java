package net.pixlies.proxy.queue;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.config.Config;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class QueueManager {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final Config config = instance.getConfig();

    private final Map<String, Queue> queues = new HashMap<>();

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
            return queue.getQueuePlayer(uuid);
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
