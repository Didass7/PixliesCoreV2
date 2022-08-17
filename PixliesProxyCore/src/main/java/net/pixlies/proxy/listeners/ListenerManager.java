package net.pixlies.proxy.listeners;

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.plugin.Listener;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.listeners.impl.profile.PixliesProfileListener;
import net.pixlies.proxy.listeners.impl.queue.QueueListener;
import net.pixlies.proxy.listeners.impl.server.ServerKickListener;

public class ListenerManager {

    private final ImmutableList<Listener> listeners = ImmutableList.of(
            new ServerKickListener(),
            new PixliesProfileListener(),
            new QueueListener()
    );

    public void registerAll() {
        listeners.forEach(listener -> PixliesProxy.getInstance().getProxy().getPluginManager().registerListener(PixliesProxy.getInstance(), listener));
    }

}
