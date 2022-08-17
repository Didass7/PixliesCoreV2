package net.pixlies.proxy.runnables.impl;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.localization.Lang;
import net.pixlies.proxy.queue.ProxyQueuePlayer;
import net.pixlies.proxy.queue.Queue;
import net.pixlies.proxy.queue.QueueManager;
import net.pixlies.proxy.runnables.PixliesRunnable;

import java.util.concurrent.TimeUnit;

public class QueueRunnable extends PixliesRunnable {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    public QueueRunnable() {
        super(10, 2, TimeUnit.SECONDS);
    }

    private int times = 0;

    @Override
    public void run() {

        for (Queue queue : manager.getQueues().values()) {

            if (queue.isPaused()) continue;
            ServerInfo info = instance.getProxy().getServerInfo(queue.getName());

            if (queue.getLimit() == 0) continue;
            if (info.getPlayers().size() >= queue.getLimit() && queue.getLimit() != -1) continue;

            ProxyQueuePlayer queuePlayer = queue.getQueuedPlayers().peek();
            if (queuePlayer == null) continue;

            ProxiedPlayer player = instance.getProxy().getPlayer(queuePlayer.getUuid());
            if (player == null) {
                queue.removeQueuePlayer(queuePlayer.getUuid());
                continue;
            }

            if (!info.canAccess(player)) {
                queue.removeQueuePlayer(queuePlayer.getUuid());
                continue;
            }

            player.connect(info);
        }

        times++;
        if (times > 5) {
            manager.requestQueueUpdate();
            times = 0;
        }

    }

}
