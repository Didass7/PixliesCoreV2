package net.pixlies.lobby.messaging;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.core.pluginmessaging.PixliesIncomingMessageListener;
import net.pixlies.core.pluginmessaging.PixliesPluginMessageManager;
import net.pixlies.lobby.messaging.impl.LeaveQueueListener;
import net.pixlies.lobby.messaging.impl.PlayerQueueListener;
import net.pixlies.lobby.messaging.impl.QueueListener;

public class PluginMessagingRegisterManager {

    private static final Main instance = Main.getInstance();
    private static final PixliesPluginMessageManager manager = instance.getPluginMessageManager();

    private final ImmutableList<PixliesIncomingMessageListener> listeners = ImmutableList.of(
            new QueueListener(),
            new PlayerQueueListener(),
            new LeaveQueueListener()
    );

    private final ImmutableList<String> outgoingChannels = ImmutableList.of(
            "queue:joinqueue"
    );

    public void registerAll() {
        outgoingChannels.forEach(manager::registerOutgoing);
        listeners.forEach(manager::registerIncoming);
    }

    public void unregisterAll() {
        outgoingChannels.forEach(manager::unregisterOutgoing);
        listeners.forEach(manager::unregisterIncoming);
    }

}
