package net.pixlies.core.listeners.user;

import com.mongodb.client.FindIterable;
import dev.morphia.query.Query;
import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    private final Main instance = Main.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User.get(player.getUniqueId()); // load the user
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        val uuid = event.getPlayer().getUniqueId();
        val user = User.get(uuid);
        user.backup();
        instance.getServer().getScheduler().runTaskLater(instance, () -> instance.getDatabase().getUserCache().remove(user.getUniqueId()), 3);
    }

}
