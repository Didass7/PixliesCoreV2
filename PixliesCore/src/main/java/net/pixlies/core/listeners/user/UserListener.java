package net.pixlies.core.listeners.user;

import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    private final Main instance = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        val uuid = event.getPlayer().getUniqueId();
        val user = User.get(uuid);
        user.backup();
        val cache = instance.getDatabase().getUserCache();
        if (!cache.containsKey(uuid)) return;
        cache.remove(uuid);
    }

}
