package net.pixlies.core.listeners.user;

import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    private final Main instance = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        instance.getDatabase().getUserCache().remove(player.getUniqueId());
        User.get(player.getUniqueId()); // load the user
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        val uuid = event.getPlayer().getUniqueId();
        val user = User.get(uuid);
        user.backup();
        instance.getDatabase().getUserCache().remove(uuid);
    }

}
