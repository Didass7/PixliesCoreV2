package net.pixlies.core.listeners;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.listeners.cosmetics.ChatEmojiListener;
import net.pixlies.core.listeners.cosmetics.JoinListener;
import net.pixlies.core.listeners.cosmetics.LeaveListener;
import net.pixlies.core.listeners.cosmetics.PlayerDeathListener;
import net.pixlies.core.listeners.moderation.*;
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
        add(new PlayerDeathListener());
        add(new GlobalPvpListener());
        add(new SlowmodeListener());
        add(new JoinListener());
        add(new LeaveListener());
    }};

    public static void registerAllListeners() {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, instance);
        }
    }

}
