package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.market.orders.OrderProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        boolean closeReason = event.getReason() == InventoryCloseEvent.Reason.CANT_USE ||
                event.getReason() == InventoryCloseEvent.Reason.PLAYER;
        if (closeReason && OrderProfile.hasProfile(player.getUniqueId())) {
            marketHandler.getProfiles().remove(player.getUniqueId().toString());
        }
    }

}
