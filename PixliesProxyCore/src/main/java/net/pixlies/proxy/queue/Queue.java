package net.pixlies.proxy.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.PriorityQueue;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Queue {

    private String name;

    private boolean paused;
    private int limit;

    private final PriorityQueue<ProxyQueuePlayer> queuedPlayers = new PriorityQueue<>(ProxyQueuePlayer::compareTo);

    public ProxyQueuePlayer getQueuePlayer(UUID uuid) {
        return queuedPlayers.stream().filter(p -> p.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public void removeQueuePlayer(UUID uuid) {
        ProxyQueuePlayer queuePlayer = getQueuePlayer(uuid);
        if (queuePlayer == null) {
            return;
        }
        queuedPlayers.remove(queuePlayer);
    }

    public int getQueuedPlayerCount() {
        return queuedPlayers.size();
    }


}
