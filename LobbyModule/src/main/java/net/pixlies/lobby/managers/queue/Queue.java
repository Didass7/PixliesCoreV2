package net.pixlies.lobby.managers.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Queue {

    private String name;

    private boolean paused;
    private int limit;
    private final Map<UUID, QueuePlayer> queuedPlayers;

    public QueuePlayer getQueuePlayer(UUID uuid) {
        return queuedPlayers.get(uuid);
    }

    public int getQueuedPlayerCount() {
        return queuedPlayers.size();
    }

}
