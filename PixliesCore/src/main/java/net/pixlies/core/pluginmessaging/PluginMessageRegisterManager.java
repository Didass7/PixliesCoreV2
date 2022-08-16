package net.pixlies.core.pluginmessaging;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;

public class PluginMessageRegisterManager {

    private static final Main instance = Main.getInstance();
    private static final PixliesPluginMessageManager manager = instance.getPluginMessageManager();

    private final ImmutableList<PixliesIncomingMessageListener> listeners = ImmutableList.of(
    );

    private final ImmutableList<String> outgoingChannels = ImmutableList.of(
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
