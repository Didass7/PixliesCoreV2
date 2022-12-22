package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.orders.OrderProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class OrderSignsListener implements Listener {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        
        if (!marketHandler.getProfiles().containsKey(player.getUniqueId().toString())) return;
        
        Sign sign = (Sign) event.getBlock().getState();
        String firstLine = String.valueOf(sign.line(0));
        OrderProfile profile = OrderProfile.get(player.getUniqueId());
        assert profile != null;
        
        if (!StringUtils.isNumericSpace(firstLine)) {
            player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
            MarketLang.MARKET_NOT_A_VALID_AMOUNT.send(player);
            player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
            return;
        }
        
        switch (profile.getSignStage()) {
            
            // CHECKING ON THE AMOUNT
            case 1 -> {
                Order order = profile.getTempOrder();
                OrderItem item = instance.getMarketManager().getBooks().get(order.getBookId()).getItem();
                
                if (order.getType() == Order.Type.SELL && Integer.parseInt(firstLine) > profile.getItemAmount(item)) {
                    player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
                    MarketLang.MARKET_NOT_ENOUGH_ITEMS.send(player);
                    player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                    return;
                }
                
                order.setAmount(Integer.parseInt(firstLine));
                // event.getBlock().setType(Material.AIR);
                profile.openPricePage(item, order.getType(), order.getAmount());
                profile.setTempOrder(null);
                profile.setTempTitle(null);
            }
            
            // CHECKING ON THE PRICE
            case 2 -> {
                // TODO: not enough money
                
                Order order = profile.getTempOrder();
                order.setPrice(Double.parseDouble(firstLine));
                event.getBlock().setType(Material.AIR);
                profile.openConfirmOrderPage(order, profile.getTempTitle());
                profile.setTempOrder(null);
                profile.setTempTitle(null);
            }
        }
    }
}
