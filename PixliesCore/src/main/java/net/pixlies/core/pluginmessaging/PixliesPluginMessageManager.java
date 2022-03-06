package net.pixlies.core.pluginmessaging;

import net.pixlies.core.Main;
import org.bukkit.plugin.messaging.Messenger;

public class PixliesPluginMessageManager {

    private static final Main instance = Main.getInstance();
    private static final Messenger messenger = instance.getServer().getMessenger();

    public void registerOutgoing(String channel) {
        messenger.registerOutgoingPluginChannel(instance, channel);
    }

    public void registerIncoming(PixliesIncomingMessageListener listener) {
        messenger.registerIncomingPluginChannel(instance, listener.getChannel().toLowerCase(), listener);
    }

    public void unregisterIncoming(PixliesIncomingMessageListener listener) {
        messenger.unregisterIncomingPluginChannel(instance, listener.getChannel().toLowerCase());
    }

    public void unregisterOutgoing(String channel) {
        messenger.unregisterOutgoingPluginChannel(instance, channel);
    }

}
