package net.pixlies.lobby.messaging.impl;

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

public class QueueListener extends PixliesIncomingMessageListener {

    private static final Lobby instance = Lobby.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    public QueueListener() {
        super("queue:queueInfo");
    }

    @Override
    public void onReceive(@NotNull String channel, @NotNull Player player, byte[] message) {
        ByteArrayInputStream stream = new ByteArrayInputStream(message);
        DataInputStream in = new DataInputStream(stream);
        try {
            String sub = in.readUTF();
            if (!sub.equalsIgnoreCase("queueInfo")) return;

            String server = in.readUTF();
            int size = in.readInt();
            int limit = in.readInt();
            boolean b = in.readBoolean();

            manager.getQueuePlayers().put(server, size);
            manager.getMaxPlayers().put(server, limit);

            if (!manager.getPausedQueue().containsKey(server) || manager.getPausedQueue().get(server) != b) {
                manager.getPausedQueue().put(server, b);
            }

        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(CC.format("&cCould not get Main information."));
        }
    }

}
