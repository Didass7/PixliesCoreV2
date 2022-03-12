package net.pixlies.core.pluginmessaging;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.core.pluginmessaging.impl.staff.StaffChatListener;

public class PluginMessageRegisterManager {

    private static final Main instance = Main.getInstance();
    private static final PixliesPluginMessageManager manager = instance.getPluginMessageManager();

    private final ImmutableList<PixliesIncomingMessageListener> listeners = ImmutableList.of(
            new StaffChatListener()
    );

    private final ImmutableList<String> outgoingChannels = ImmutableList.of(
            "pixlies:staffchat"
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
