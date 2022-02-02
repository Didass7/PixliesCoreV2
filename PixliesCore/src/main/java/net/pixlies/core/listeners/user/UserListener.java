package net.pixlies.core.listeners.user;

import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    private final Main instance = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        val uuid = event.getPlayer().getUniqueId();
        val cache = instance.getDatabase().getUserCache();
        if (!cache.containsKey(uuid)) return;
        cache.remove(uuid);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        // UNSET STAFFMODE IF NOT HAVE PERM
        val player = event.getPlayer();
        if (player.hasPermission("pixlies.staff.staffmode")) return;
        val user = User.get(player.getUniqueId());
        user.getSettings().setStaffModeEnabled(false);
    }

}
