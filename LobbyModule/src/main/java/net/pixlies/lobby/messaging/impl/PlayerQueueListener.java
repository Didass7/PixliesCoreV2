package net.pixlies.lobby.messaging.impl;

import net.pixlies.core.localization.Lang;
import net.pixlies.core.pluginmessaging.PixliesIncomingMessageListener;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class PlayerQueueListener extends PixliesIncomingMessageListener {

    private static final Lobby instance = Lobby.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    public PlayerQueueListener() {
        super("queue:queueposition");
    }

    @Override
    public void onReceive(@NotNull String channel, @NotNull Player player, byte[] message) {
        ByteArrayInputStream stream = new ByteArrayInputStream(message);
        DataInputStream in = new DataInputStream(stream);

        try {
            String u = in.readUTF();
            UUID uuid = UUID.fromString(u);

            String server = in.readUTF();
            int position = in.readInt();

            Player p = Bukkit.getPlayer(uuid);
            if (p == null) return;

            if (manager.isInQueue(p) && manager.getQueuePosition(p) == -1) {
                p.sendMessage(Lang.PIXLIES + CC.format("&7An error has occurred with the queue! Please rejoin."));
                manager.getQueueMap().remove(uuid);
                manager.getPlayerPosition().remove(uuid);
                return;
            }
            manager.getQueueMap().put(uuid, server);
            manager.getPlayerPosition().put(uuid, position);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
