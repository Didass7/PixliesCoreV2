package net.pixlies.proxy.queue;

import lombok.*;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.config.Config;

import java.util.PriorityQueue;
import java.util.UUID;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Queue {
    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final Config config = instance.getConfig();

    private @Getter String name;

    private @Getter boolean paused;

    private @Getter int limit;

    private final @Getter PriorityQueue<ProxyQueuePlayer> queuedPlayers = new PriorityQueue<>(ProxyQueuePlayer::compareTo);
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

    public void setPaused(boolean paused) {
        config.getConfig().set("queues." + name + ".paused", paused);
        config.save();
        this.paused = paused;
    }

    public void setLimit(int limit) {
        config.getConfig().set("queues." + name + ".limit", limit);
        config.save();
        this.limit = limit;
    }

}
