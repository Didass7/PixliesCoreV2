package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.util.DeadOrdersUtil;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
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
    
    private final String bookItem;
    private final String orderId;
    
    @Setter
    private long timestamp;
    
    private final Type type;
    private final UUID playerUUID;
    
    @Setter
    private BigDecimal price;
    
    @Setter
    private int amount;
    
    private int volume;
    
    private final List<Trade> trades;
    private final Map<BigDecimal, Boolean> refunds;
    
    public Order(Type type, String bookItem, long timestamp, UUID playerUUID, BigDecimal price, int amount) {
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
    
    // Used to display price for order items
    public BigDecimal getTaxedPrice() {
        NationProfile profile = NationProfile.get(playerUUID);
        Nation nation = Nation.getFromId(profile.getNationId());
        if (nation == null)
            return price;
        return price.multiply(nation.getTaxRate().add(BigDecimal.valueOf(1)));
    }
    
    // Used to display price for recent orders and refunds
    public BigDecimal getTariffedPrice(UUID matchingUUID) {
        String initId = NationProfile.get(playerUUID).getNationId();
        String matchId = NationProfile.get(matchingUUID).getNationId();
    
        if (playerUUID == matchingUUID)
            return price;
    
        for (Tariff tariff : Tariff.getAll()) {
            if (Objects.equals(tariff.getInitId(), initId) && Objects.equals(tariff.getTargetId(), matchId)) {
                return price.multiply(tariff.getRate().add(BigDecimal.valueOf(1)));
            }
        }
    
        return price;
    }
    
    /**
     * Զըխխը՛մ։
     */
    // Used to display price for trades
    public BigDecimal getTaxedTariffedPrice(UUID matchingUUID) {
        String initId = NationProfile.get(playerUUID).getNationId();
        String matchId = NationProfile.get(matchingUUID).getNationId();
        
        if (Nation.getFromId(initId) == null)
            return price;
        BigDecimal taxRate = Nation.getFromId(initId).getTaxRate();
    
        if (playerUUID == matchingUUID)
            return price;
    
        for (Tariff tariff : Tariff.getAll()) {
            if (Objects.equals(tariff.getInitId(), initId) && Objects.equals(tariff.getTargetId(), matchId)) {
                return price.multiply((taxRate.add(tariff.getRate()).add(BigDecimal.valueOf(1))));
            }
        }
        
        return price;
    }
    
    public void cancel() {
        OrderBook book = OrderBook.get(bookItem);
        switch (type) {
            case BUY -> book.getBuyOrders().remove(this);
            case SELL -> book.getSellOrders().remove(this);
        }
        book.save();
        DeadOrdersUtil.backupDeadOrder(this);
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
    
    public BigDecimal getCoinsToClaim() {
        BigDecimal coins = BigDecimal.valueOf(0.0);
        for (Trade trade : trades) {
            if (trade.isClaimed()) continue;
            coins = coins.add(trade.getPrice().multiply(BigDecimal.valueOf(trade.getAmount())));
        }
        return coins;
    }
    
    public BigDecimal getTotalRefunds() {
        BigDecimal totalRefunds = BigDecimal.valueOf(0.0);
        for (BigDecimal value : refunds.keySet()) {
            totalRefunds = totalRefunds.add(value);
        }
        return totalRefunds;
    }
    
    public BigDecimal getRefundableCoins() {
        BigDecimal claimableRefunds = BigDecimal.valueOf(0.0);
        for (Map.Entry<BigDecimal, Boolean> entry : refunds.entrySet()) {
            if (entry.getValue()) continue;
            claimableRefunds = claimableRefunds.add(entry.getKey());
        }
        return claimableRefunds;
    }
    
    public void refundPlayer() {
        NationProfile profile = NationProfile.get(playerUUID);
        for (Map.Entry<BigDecimal, Boolean> entry : refunds.entrySet()) {
            if (entry.getValue()) continue;
            profile.addBalance(entry.getKey());
            refunds.put(entry.getKey(), true);
        }
        profile.save();
        save();
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
    
    // Աստուծոյ սիրոյն, աշխատի՛ր։
    // Վա՜յ անասուն, վա՜յ։
    public String toString(UUID initialUUID) {
        String playerName = Objects.requireNonNull(Bukkit.getPlayer(playerUUID)).getName();
        String prefix = type == Order.Type.BUY ? "§a§lBUY §r§a" : "§6§lSELL §r§6";
        BigDecimal price = getTariffedPrice(initialUUID);
        return " §8» " + prefix + amount + "§8x §7@ §6" + price.doubleValue() + "§7 each from §b" + playerName;
    }
    
    @Override
    public String toString() {
        String prefix = type == Order.Type.BUY ? "§a§lBUY §r§a" : "§6§lSELL §r§6";
        return " §8» " + prefix + amount + "§8x §7@ §6" + getTaxedPrice().doubleValue() + "§7 each §8(with tax)";
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
