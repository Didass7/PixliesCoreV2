package net.pixlies.core.listeners;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.listeners.cosmetics.PollListener;
import net.pixlies.core.listeners.cosmetics.SitListener;
import net.pixlies.core.listeners.cosmetics.chatformatting.ChatFormattingListener;
import net.pixlies.core.listeners.moderation.*;
import net.pixlies.core.listeners.player.AfkListener;
import net.pixlies.core.listeners.player.JoinQuitListener;
import net.pixlies.core.listeners.player.SocialSpyListener;
import net.pixlies.core.listeners.player.TeleportListener;
import net.pixlies.core.listeners.staff.StaffChatListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private static final Main instance = Main.getInstance();

    private @Getter static final List<Listener> listeners = new ArrayList<>() {{
        add(new BanListener());
        add(new MuteListener());
        add(new ChatModerationListener());
        add(new ChatFormattingListener());
        add(new GlobalPvpListener());
        add(new SlowModeListener());
        add(new JoinQuitListener());
        add(new CommandSpyListener());
        add(new TeleportListener());
        add(new PassiveListener());
        add(new SocialSpyListener());
        add(new VanishListener());
        add(new StaffModeListener());
        add(new AfkListener());
        add(new SitListener());
        add(new PollListener());
        add(new BlackListListener());
        add(new FreezeListener());
        add(new StaffChatListener());
    }};

    public static void registerAllListeners() {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, instance);
        }
    }

}
