package net.pixlies.lobby.managers.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Queue {

    private final String name;

    private final boolean paused;
    private final int limit;
    private final int size;
    private final Map<UUID, QueuePlayer> queuedPlayers;

    public QueuePlayer getQueuePlayer(UUID uuid) {
        return queuedPlayers.get(uuid);
    }

    public int getQueuedPlayerCount() {
        return queuedPlayers.size();
    }

    public boolean isOneMoreFull() {
        return queuedPlayers.size() - 1 >= limit;
    }

}
