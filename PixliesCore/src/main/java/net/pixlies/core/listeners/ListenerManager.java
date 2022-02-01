package net.pixlies.core.listeners;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.listeners.cosmetics.ChatEmojiListener;
import net.pixlies.core.listeners.moderation.*;
import net.pixlies.core.listeners.player.JoinListener;
import net.pixlies.core.listeners.player.QuitListener;
import net.pixlies.core.listeners.player.TeleportListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private static final Main instance = Main.getInstance();

    private @Getter static final List<Listener> listeners = new ArrayList<>(){{
        add(new BanListener());
        add(new MuteListener());
        add(new ChatModerationListener());
        add(new ChatEmojiListener());
        add(new GlobalPvpListener());
        add(new SlowModeListener());
        add(new JoinListener());
        add(new QuitListener());
        add(new CommandSpyListener());
        add(new TeleportListener());
        add(new VanishListener());
    }};

    public static void registerAllListeners() {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, instance);
        }
    }

}
