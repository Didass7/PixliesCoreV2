package net.pixlies.lobby.managers;

import lombok.Getter;
import net.pixlies.core.database.redis.RedisManager;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.core.utils.json.JsonBuilder;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.queue.Queue;
import net.pixlies.lobby.managers.queue.QueuePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class QueueManager {

    private static final Lobby instance = Lobby.getInstance();
    private static final ModuleConfig config = instance.getConfig();

    private final @Getter Map<String, Queue> queues = new HashMap<>();


    public Collection<QueuePlayer> getAllQueuedPlayers() {
        List<QueuePlayer> toReturn = new ArrayList<>();
        queues.forEach((queueName, queue) -> toReturn.addAll(queue.getQueuedPlayers().values()));
        return toReturn;
    }

    public Collection<UUID> getAllQueuedPlayersUuid() {
        List<UUID> toReturn = new ArrayList<>();
        queues.forEach((queueName, queue) -> queue.getQueuedPlayers().forEach((k, queuePlayer) -> toReturn.add(queuePlayer.getUuid())));
        return toReturn;
    }

    public boolean isInQueue(UUID uuid) {
        return getAllQueuedPlayersUuid().contains(uuid);
    }

    public @Nullable Queue getQueueFromPlayer(UUID uuid) {
        return queues.values().stream().filter(queue -> queue.getQueuePlayer(uuid) != null).findFirst().orElse(null);
    }

    public @Nullable QueuePlayer getQueuePlayer(UUID uuid) {
        for (Queue queue : queues.values()) {
            QueuePlayer queuePlayer = queue.getQueuePlayer(uuid);
            if (queuePlayer == null) continue;
            return queuePlayer;
        }
        return null;
    }

    public @Nullable Queue getQueue(String name) {
        return queues.get(name);
    }

    public void addPlayerToQueue(Player player, String server) {
        RedisManager.sendRequest("Queue.JoinQueue", new JsonBuilder()
                .addProperty("uuid", player.getUniqueId().toString())
                .addProperty("server", server)
                .addProperty("priority", getPriority(player))
                .toJsonObject());
    }

    public void removePlayerFromQueue(Player player) {
        RedisManager.sendRequest("Queue.LeaveQueue", new JsonBuilder()
                .addProperty("uuid", player.getUniqueId().toString())
                .toJsonObject());
    }

    public static int getPriority(Player player) {
        for (int i = 100; i > 0; i--) {
            if (player.hasPermission("pixlies.queue.priority." + i)) {
                return i;
            }
        }
        return 0;
    }

}
