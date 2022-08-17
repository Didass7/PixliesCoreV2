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

public class QueueMessageRunnable extends PixliesRunnable {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    public QueueMessageRunnable() {
        super(1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        for (ProxiedPlayer player : instance.getProxy().getPlayers()) {
            // NOT IN QUEUE
            if (!manager.isInQueue(player.getUniqueId())) continue;

            ProxyQueuePlayer queuePlayer = manager.getQueuePlayer(player.getUniqueId());
            if (queuePlayer == null) continue;

            Queue queue = manager.getQueueFromPlayer(player.getUniqueId());
            if (queue == null) continue;

            // SERVER ACCESS CHECK
            ServerInfo serverInfo = instance.getProxy().getServerInfo(queue.getName());
            if (serverInfo == null) continue;
            if (serverInfo.isRestricted() && !player.hasPermission(serverInfo.getPermission())) {
                Lang.PLAYER_QUEUE_NO_PERMISSION.send(player);
                continue;
            }

            if (queue.isPaused()) {
                Lang.PLAYER_QUEUE_UPDATE_SERVER_PAUSED_MESSAGE.send(player,
                        "%SERVER%;" + queue.getName(),
                        "%POSITION%;" + manager.getPosition(player.getUniqueId()),
                        "%TOTAL%;" + queue.getQueuedPlayers().size());
                continue;
            }

            Lang.PLAYER_QUEUE_UPDATE_MESSAGE.send(player,
                    "%SERVER%;" + queue.getName(),
                    "%POSITION%;" + manager.getPosition(player.getUniqueId()),
                    "%TOTAL%;" + queue.getQueuedPlayers().size());

        }
    }

}
