package net.pixlies.proxy.listeners.impl.queue;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.database.redis.RedisMessageReceiveEvent;
import net.pixlies.proxy.localization.Lang;
import net.pixlies.proxy.queue.ProxyQueuePlayer;
import net.pixlies.proxy.queue.Queue;
import net.pixlies.proxy.queue.QueueManager;

import java.util.UUID;

public class QueueListener implements Listener {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    @EventHandler
    public void onReceive(RedisMessageReceiveEvent event) {
        JsonObject jsonObject = event.getData();
        switch (event.getIdentifier()) {
            case "Queue.JoinQueue" -> {
                UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
                String server = jsonObject.get("server").getAsString();
                int priority = jsonObject.get("priority").getAsInt();

                ProxiedPlayer player = instance.getProxy().getPlayer(uuid);
                if (manager.isInQueue(uuid)) {
                    if (player != null) {
                        Lang.PLAYER_QUEUE_ALREADY_IN.send(player);
                    }
                    return;
                }

                Queue queue = manager.getQueue(server);
                if (queue == null) {
                    if (player != null) {
                        Lang.PLAYER_QUEUE_NOT_EXIST.send(player, "%SERVER%;" + server);
                    }
                    return;
                }

                if (priority == 100) {
                    if (player == null) break;
                    ServerInfo info = instance.getProxy().getServerInfo(queue.getName());
                    if (info == null) break;
                    if (!info.canAccess(player)) break;

                    player.connect(info);
                    Lang.PLAYER_SERVER_CONNECTING.send(player, "%SERVER%;" + info.getName());
                    return;
                }

                manager.addPlayerToQueue(server, new ProxyQueuePlayer(uuid, priority, System.currentTimeMillis()));

                if (player != null) {
                    Lang.PLAYER_QUEUE_JOIN.send(player, "%SERVER%;" + queue.getName());
                }
            }

            case "Queue.LeaveQueue" -> {
                UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

                ProxiedPlayer player = instance.getProxy().getPlayer(uuid);
                if (!manager.isInQueue(uuid)) {
                    if (player != null) {
                        Lang.PLAYER_QUEUE_NOT_IN.send(player);
                    }
                    return;
                }

                Queue queue = manager.removePlayerFromQueue(uuid);
                if (queue == null) return;

                if (player != null) {
                    Lang.PLAYER_QUEUE_LEAVE.send(player, "%SERVER%;" + queue.getName());
                }
            }
        }
    }

    @EventHandler
    public void onDisconnect(ServerDisconnectEvent event) {
        manager.removePlayerFromQueue(event.getPlayer().getUniqueId());
        manager.requestQueueUpdate();
    }

}
