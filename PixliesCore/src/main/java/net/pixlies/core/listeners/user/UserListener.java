package net.pixlies.core.listeners.user;

import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

    private final Main instance = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        instance.getMongoManager().getUserCache().remove(uuid);
        User user = User.get(uuid);
        user.load(true);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        instance.getServer().getScheduler().runTaskLater(instance, () -> {
            if (!player.isOnline()) return;
            User user = User.get(player.getUniqueId());
            if (!user.isLoaded()) {
                player.kickPlayer(CC.format("&cYour profile has failed to load.\nIf this error persists, please contact a staff member."));
                return;
            }

            if (!user.getKnownUsernames().contains(player.getName())) {
                user.getKnownUsernames().add(player.getName());
                user.save();
            }

            if (user.hasJoinedBefore()) {
                Lang.PLAYER_PROFILE_LOAD.send(player);
            } else {
                Lang.PLAYER_PROFILE_CREATED.send(player);
            }
        }, 60); // 3s
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        val uuid = event.getPlayer().getUniqueId();
        val user = User.get(uuid);
        user.removeFromCache();
    }

}
