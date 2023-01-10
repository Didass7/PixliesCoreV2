package net.pixlies.business.market.profiles;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.Trade;
import net.pixlies.core.entity.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Market profile.
 *
 * @author vyketype
 */
@Data
// TODO: to be removed...
public class OrderProfile {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    
    @Getter(AccessLevel.NONE)
    private final UUID uuid;
    
    // ----------------------------------------------------------------------------------------------------
    // OTHER METHODS
    // ----------------------------------------------------------------------------------------------------
    
    // TODO: REFUNDING EXTRA MONEY + DON'T FORGET TO UPDATE THE BOOLEAN ON CLAIMING THE REFUND
    
    public void refundGoods(Order order) {
        // todo: rework also switch the things bc it's in reverse (buy and sell)
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        User user = User.get(uuid);
        OrderBook book = OrderBook.get(order.getBookItem());
        if (order.getType() == Order.Type.BUY) {
            Material material = book.getItem().getMaterial();
            for (int i = 0; i < order.getVolume(); i++) player.getInventory().addItem(new ItemStack(material));
            MarketLang.ORDER_ITEMS_REFUNDED.send(player, "%AMOUNT%;" + order.getVolume(),
                    "%ITEM%;" + book.getItem().getName());
        } else {
            // TODO: add coins to wallet
            user.save();
            MarketLang.ORDER_COINS_REFUNDED.send(player, "%COINS%" + (order.getVolume() * order.getTaxedPrice()));
        }
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
    }
    
    public void claimGoods(Order order) {
        // todo if there are no items to claim
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        User user = User.get(uuid);
        OrderBook book = OrderBook.get(order.getBookItem());
        if (order.getType() == Order.Type.BUY) {
            int items = 0;
            for (Trade t : order.getTrades()) {
                if (t.isClaimed()) continue;
                items += t.getAmount();
                t.claim();
                t.save();
            }
            
            Material material = book.getItem().getMaterial();
            for (int i = 0; i < items; i++) player.getInventory().addItem(new ItemStack(material));
            
            MarketLang.ORDER_ITEMS_CLAIMED.send(player, "%NUM%;" + items, "%AMOUNT%;" + order.getAmount(),
                    "%ITEM%;" + book.getItem().getName());
        } else {
            int coins = 0;
            for (Trade t : order.getTrades()) {
                if (t.isClaimed()) continue;
                coins += t.getAmount() * t.getPrice();
                t.claim();
                t.save();
            }
            
            // TODO: add coins to wallet
            user.save();
            
            MarketLang.ORDER_ITEMS_CLAIMED.send(player, "%COINS%" + coins, "%AMOUNT%;" + order.getAmount(),
                    "%ITEM%;" + book.getItem().getName());
        }
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
    }
}
