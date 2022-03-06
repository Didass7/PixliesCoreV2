package net.pixlies.core.pluginmessaging;

import net.pixlies.core.Main;
import org.bukkit.plugin.messaging.Messenger;

/**
 * Plugin messaging manager
 * @author Dynmie
 */
public class PixliesPluginMessageManager {

    private static final Main instance = Main.getInstance();
    private static final Messenger messenger = instance.getServer().getMessenger();

    /**
     * Register an outgoing channel.
     * Channel should be lowercase. If it is not lowercase, it will force it to be lowercase.
     * @param channel Lowercase channel name.
     */
    public void registerOutgoing(String channel) {
        messenger.registerOutgoingPluginChannel(instance, channel.toLowerCase());
    }

    /**
     * Register an incoming channel.
     * Channel should be lowercase. If it is not lowercase, it will force it to be lowercase.
     * @param listener The listener.
     * @see PixliesIncomingMessageListener
     */
    public void registerIncoming(PixliesIncomingMessageListener listener) {
        messenger.registerIncomingPluginChannel(instance, listener.getChannel().toLowerCase(), listener);
    }

    /**
     * Unregister an outgoing channel.
     * Channel should be lowercase. If it is not lowercase, it will force it to be lowercase.
     * @param channel Lowercase channel name.
     */
    public void unregisterOutgoing(String channel) {
        messenger.unregisterOutgoingPluginChannel(instance, channel.toLowerCase());
    }

    /**
     * Unregister an incoming channel.
     * Channel should be lowercase. If it is not lowercase, it will force it to be lowercase.
     * @param listener The listener.
     * @see PixliesIncomingMessageListener
     */
    public void unregisterIncoming(PixliesIncomingMessageListener listener) {
        messenger.unregisterIncomingPluginChannel(instance, listener.getChannel().toLowerCase());
    }

}
