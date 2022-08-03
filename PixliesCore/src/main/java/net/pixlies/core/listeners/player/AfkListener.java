package net.pixlies.core.listeners.player;

import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.purpurmc.purpur.event.PlayerAFKEvent;

public class AfkListener implements Listener {

    @EventHandler
    public void onAfk(PlayerAFKEvent event) {

        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());

        if (user.isPassive()) return;

        if (event.isGoingAfk()) {
            Lang.PLAYER_AFK_ON_BROADCAST.broadcast("%PLAYER%;" + player.getName());
        } else {
            Lang.PLAYER_AFK_OFF_BROADCAST.broadcast("%PLAYER%;" + player.getName());
        }

    }

}
