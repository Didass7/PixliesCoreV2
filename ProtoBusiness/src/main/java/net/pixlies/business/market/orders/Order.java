package net.pixlies.business.market.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.market.nations.Tariff;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;

import java.util.*;

/**
 * Orders.
 *
 * @author vyketype
 */
@Getter
@AllArgsConstructor
public class Order {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);
    
    private final String bookItem;
    private final String orderId;
    
    @Setter
    private long timestamp;
    
    private final Type type;
    private final UUID playerUUID;
    
    @Setter
    private double price;
    
    @Setter
    private int amount;
    
    private int volume;
    
    private final List<Trade> trades;
    private final Map<Double, Boolean> refunds;
    
    public Order(Type type, String bookItem, long timestamp, UUID playerUUID, double price, int amount) {
        orderId = TextUtils.generateId(9);
        this.bookItem = bookItem;
        this.type = type;
        this.timestamp = timestamp;
        this.playerUUID = playerUUID;
        this.price = price;
        this.amount = amount;
        volume = amount;
        trades = new LinkedList<>();
        refunds = new HashMap<>();
    }
    
    /**
     * Best shit you've ever seen.
     * Զըխխը՛մ։
     */
    public double getRelativePrice(UUID matchingUUID) {
        if (playerUUID == matchingUUID) return price;
        
        String initId = NationProfile.get(playerUUID).getNationId();
        String matchId = NationProfile.get(matchingUUID).getNationId();
        
        double rate = Nation.getFromId(initId).getTaxRate();
        
        for (Tariff tariff : Tariff.getAll()) {
            if (Objects.equals(tariff.getInitId(), initId) && Objects.equals(tariff.getTargetId(), matchId)) {
                return price * (1 + rate + tariff.getRate());
            }
        }
        
        return price;
    }
    
    public boolean isCancellable() {
        for (Trade trade : trades) {
            if (!trade.isClaimed()) return false;
        }
        return true;
    }
    
    public int getItemsToClaim() {
        int items = 0;
        for (Trade trade : trades) {
            if (trade.isClaimed()) continue;
            items += trade.getAmount();
        }
        return items;
    }
    
    public double getCoinsToClaim() {
        double coins = 0;
        for (Trade trade : trades) {
            if (trade.isClaimed()) continue;
            coins += trade.getPrice() * trade.getAmount();
        }
        return coins;
    }
    
    public double getTotalRefunds() {
        double totalRefunds = 0;
        for (Double value : refunds.keySet()) {
            totalRefunds += value;
        }
        return totalRefunds;
    }
    
    public double getRefundableCoins() {
        double claimableRefunds = 0;
        for (Map.Entry<Double, Boolean> entry : refunds.entrySet()) {
            if (entry.getValue()) continue;
            claimableRefunds += entry.getKey();
        }
        return claimableRefunds;
    }
    
    public void decreaseVolume(int amount) {
        volume -= amount;
    }
    
    public void save() {
        OrderBook book = OrderBook.get(bookItem);
        if (type == Order.Type.BUY) {
            for (Order order : book.getBuyOrders()) {
                if (!Objects.equals(order.getOrderId(), orderId)) continue;
                book.getBuyOrders().set(book.getBuyOrders().indexOf(order), this);
                book.save();
                return;
            }
            book.getBuyOrders().add(this);
        } else {
            for (Order order : book.getSellOrders()) {
                if (!Objects.equals(order.getOrderId(), orderId)) continue;
                book.getSellOrders().set(book.getBuyOrders().indexOf(order), this);
                book.save();
                return;
            }
            book.getSellOrders().add(this);
        }
        book.save();
    
        instance.logInfo("Saved order " + orderId + " to the CACHE.");
    }
    
    // --------------------------------------------------------------------------------------------
    
    public static Order get(String orderId) {
        for (OrderBook book : OrderBook.getAll()) {
            for (Order order : book.getBuyOrders()) {
                if (Objects.equals(order.getOrderId(), orderId)) return order;
            }
            for (Order order : book.getSellOrders()) {
                if (Objects.equals(order.getOrderId(), orderId)) return order;
            }
        }
        return null;
    }
    
    // --------------------------------------------------------------------------------------------
    
    public enum Type {
        BUY,
        SELL
    }
}
