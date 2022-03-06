package net.pixlies.lobby.messaging.impl;

import net.pixlies.core.pluginmessaging.PixliesIncomingMessageListener;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class LeaveQueueListener extends PixliesIncomingMessageListener {

    private static final Lobby instance = Lobby.getInstance();
    private static final QueueManager manager = instance.getQueueManager();

    public LeaveQueueListener() {
        super("queue:leavequeue");
    }

    @Override
    public void onReceive(@NotNull String channel, @NotNull Player player, byte[] message) {
        ByteArrayInputStream stream = new ByteArrayInputStream(message);
        DataInputStream in = new DataInputStream(stream);
        try {

            String u = in.readUTF();
            UUID uuid = UUID.fromString(u);

            manager.getPlayerPosition().remove(uuid);
            manager.getQueueMap().remove(uuid);

        } catch (IOException e) {
            instance.getLogger().info(CC.format("&cCould not get queue position information."));
        }
    }

}
