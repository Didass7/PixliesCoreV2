package net.pixlies.business.market.orders;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
    
    private final String orderId;
    private final String bookId;
    private @Setter
    long timestamp;
    
    private final Type type;
    
    private final UUID playerUUID;
    private @Setter
    double price;
    private @Setter
    int amount;
    private int volume;
    
    private final List<Trade> trades;
    
    public Order(Document document) {
        this(
                document.getString("orderId"),
                document.getString("bookId"),
                document.getLong("timestamp"),
                Type.valueOf(document.getString("type")),
                UUID.fromString(document.getString("playerUUID")),
                document.getDouble("price"),
                document.getInteger("amount"),
                document.getInteger("volume"),
                new ArrayList<>() {{
                    for (Document tradeDoc : document.getList("trades", Document.class)) {
                        add(new Trade(tradeDoc));
                    }
                }}
        );
    }
    
    public Document toDocument() {
        Document document = new Document();
        
        document.put("orderId", orderId);
        document.put("bookId", bookId);
        document.put("timestamp", timestamp);
        document.put("type", type.name());
        document.put("playerUUID", playerUUID.toString());
        document.put("price", price);
        document.put("amount", amount);
        document.put("volume", volume);
        document.put("trades", new ArrayList<Document>() {{
            for (Trade trade : trades) {
                add(trade.toDocument());
            }
        }});
        
        return document;
    }
    
    public Order(Type type, String bookId, long timestamp, UUID uuid, double price, int amount) {
        orderId = TextUtils.generateId(7);
        this.bookId = bookId;
        this.type = type;
        this.timestamp = timestamp;
        playerUUID = uuid;
        this.price = price;
        this.amount = amount;
        volume = amount;
        trades = new LinkedList<>();
    }
    
    public double getPrice(UUID matchingUUID) {
        if (playerUUID == matchingUUID) return price;
        
        String from = NationProfile.get(playerUUID).getNation().getNationId();
        String to = NationProfile.get(matchingUUID).getNation().getNationId();
        double rate = 0;
        
        // Checks the tariffs list and sees if there are any that match
        for (Tariff tariff : instance.getMarketManager().getTariffs().values()) {
            if (Objects.equals(tariff.getFrom(), from) && Objects.equals(tariff.getTo(), to)) {
                rate = tariff.getRate();
                break;
            }
        }
        
        return price * (1 + rate);
    }
    
    public boolean isCancellable() {
        for (Trade t : trades) {
            if (!t.isClaimed()) return true;
        }
        return false;
    }
    
    public void decreaseVolume(int amount) {
        volume -= amount;
    }
    
    public void sendNotification() {
        Player player = Bukkit.getPlayer(playerUUID);
        
        // If player is offline
        if (player == null) {
            marketHandler.getNotifs().put(playerUUID.toString(), this);
            return;
        }
        
        OrderItem item = instance.getMarketManager().getBooks().get(bookId).getItem();
        if (type == Type.BUY)
            MarketLang.BUY_ORDER_FILLED.send(player, "%NUM%;" + amount, "%ITEM%;" + item.getName());
        else MarketLang.SELL_ORDER_FILLED.send(player, "%NUM%;" + amount, "%ITEM%;" + item.getName());
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
    }
    
    public enum Type {
        BUY, SELL
    }
}
