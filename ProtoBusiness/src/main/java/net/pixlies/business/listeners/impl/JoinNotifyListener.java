package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.market.orders.Order;
import net.pixlies.core.entity.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinNotifyListener implements Listener {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (!marketHandler.getNotifs().containsKey(uuid)) return;
        for (Order order : marketHandler.getNotifs().get(uuid)) order.sendNotification();

    }

}
