package net.pixlies.proxy.runnables.impl;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.handlers.impl.AutoAnnounceHandler;
import net.pixlies.proxy.runnables.PixliesRunnable;

import java.util.concurrent.TimeUnit;

public class AutoAnnounceRunnable extends PixliesRunnable {

    private static final Proxy instance = Proxy.getInstance();
    private final AutoAnnounceHandler handler = instance.getHandlerManager().getHandler(AutoAnnounceHandler.class);

    public AutoAnnounceRunnable() {
        super(0, 0, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        if (!handler.isEnabled()) return;
        String message = handler.getFirstAndReplace();
        if (message == null)
            return;
        for (ProxiedPlayer player : instance.getProxy().getPlayers()) {
            player.sendMessage(new TextComponent(message));
        }
    }

}
