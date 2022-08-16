package net.pixlies.core.listeners;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.listeners.cosmetics.ChatFormattingListener;
import net.pixlies.core.listeners.cosmetics.PollListener;
import net.pixlies.core.listeners.cosmetics.SitListener;
import net.pixlies.core.listeners.moderation.*;
import net.pixlies.core.listeners.player.*;
import net.pixlies.core.listeners.staff.StaffChatListener;
import net.pixlies.core.listeners.staff.StaffJoinQuitListener;
import net.pixlies.core.listeners.user.UserListener;
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
//        add(new GlobalPvpListener()); TODO: bugged
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
        add(new StaffJoinQuitListener());
        add(new UserListener());
        add(new RespawnListener());
        add(new SpeedListener());
        add(new CombatListener());
    }};

    public static void registerAllListeners() {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, instance);
        }
    }

}
