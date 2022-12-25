package net.pixlies.core.listeners.user;

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

import java.net.InetSocketAddress;
import java.util.UUID;

public class UserListener implements Listener {

    private final Main instance = Main.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        instance.getMongoManager().getUserCache().remove(uuid);

        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            User user = User.get(uuid);
            user.load(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        instance.getServer().getScheduler().runTaskLater(instance, () -> {
            if (!player.isOnline()) return;
            User user = User.get(player.getUniqueId());

            if (!user.isLoaded()) {
                player.kickPlayer(CC.format("&cYour profile has failed to load.\nIf this error persists, please contact a staff member.\n\nError: " + getClass().getName()));
                return;
            }

            user.setCurrentUsername(player.getName());
            if (!user.getKnownUsernames().contains(player.getName())) {
                user.getKnownUsernames().add(player.getName());
            }

            InetSocketAddress address = player.getAddress();
            if (address != null) {
                user.setCurrentIp(address.getAddress().getHostAddress());
                if (!user.getKnownIps().contains(address.getAddress().getHostAddress())) {
                    user.getKnownIps().add(address.getAddress().getHostAddress());
                }
            }

            user.save();

            if (user.hasJoinedBefore()) {
                Lang.PLAYER_PROFILE_LOAD.send(player);
            } else {
                Lang.PLAYER_PROFILE_CREATED.send(player);
            }
        }, 60); // 3s
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        final var uuid = event.getPlayer().getUniqueId();
        instance.getMongoManager().getUserCache().remove(uuid);
    }

}
