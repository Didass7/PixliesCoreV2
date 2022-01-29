package net.pixlies.core.listeners.cosmetics;

import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("§8[§a+§8] §7" + event.getPlayer().getName());
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        Player player = (Player) event.getPlayerProfile();
        String kickMsg = Lang.STAFFMODE_KICK_MESSAGE.get(player);
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMsg);
    }

}
