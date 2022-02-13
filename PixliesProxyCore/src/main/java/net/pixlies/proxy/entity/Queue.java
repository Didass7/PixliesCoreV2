package net.pixlies.proxy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.Proxy;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Queue {

    private static final Proxy instance = Proxy.getInstance();

    private final String serverName;
    private final Map<Integer, UUID> queuedPlayers = new TreeMap<>();
    private final String permissionNode;
    private int maxPlayers;

    public ServerInfo getServerInfo() {
        return instance.getProxy().getServerInfo(serverName);
    }

    public void addPlayer(ProxiedPlayer player) {
        for (int i = 100; i >= 0 ; i--) {
            if (player.hasPermission("pixlies.queue." + serverName + ".priority." + i)) {
                queuedPlayers.put(i, player.getUniqueId());
                return;
            }
        }
        queuedPlayers.put(0, player.getUniqueId());
    }

    public void removePlayer(ProxiedPlayer player) {
        for (Map.Entry<Integer, UUID> entry : queuedPlayers.entrySet()) {
            int priority = entry.getKey();
            UUID uuid = entry.getValue();
            if (uuid == player.getUniqueId()) {
                queuedPlayers.remove(priority, uuid);
                return;
            }
        }
        queuedPlayers.remove(0, player.getUniqueId());
    }

    public ProxiedPlayer getFirstQueuedPlayer() {
        if (queuedPlayers.isEmpty()) return null;
        UUID uuid = queuedPlayers.get(0);
        return instance.getProxy().getPlayer(uuid);
    }

}
