package net.pixlies.business.listeners.impl;

import net.pixlies.business.market.orders.Order;
import net.pixlies.core.entity.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinNotifyListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());

        if (user.getNotifs().isEmpty()) return;

        for (Object o : user.getNotifs()) {
            Order order = (Order) o;
            order.sendNotification();
        }
    }

}
