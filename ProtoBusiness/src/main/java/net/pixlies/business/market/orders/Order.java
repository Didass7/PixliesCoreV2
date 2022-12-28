package net.pixlies.business.market.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bson.Document;

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
    
    /**
     * Best shit you've ever seen.
     * Զըխխը՛մ։
     */
    public double getPrice(UUID matchingUUID) {
        if (playerUUID == matchingUUID) return price;
        
        String initId = NationProfile.get(playerUUID).getNationId();
        String matchId = NationProfile.get(matchingUUID).getNationId();
        
        // Get matching tariffs
        List<Tariff> initTariffs = new ArrayList<>();
        List<Tariff> matchedTariffs = new ArrayList<>();
        for (Tariff tariff : Tariff.getAll()) {
            if (Objects.equals(tariff.getInitId(), initId) && Objects.equals(tariff.getTargetId(), matchId)) {
                initTariffs.add(tariff);
            }
            if (Objects.equals(tariff.getInitId(), matchId) && Objects.equals(tariff.getTargetId(), initId)) {
                matchedTariffs.add(tariff);
            }
        }
        
        double rate = 0.00;
        
        for (Tariff tariff : initTariffs) {
            if (tariff.getType() == Tariff.Type.EXPORTS && type == Type.SELL) {
                rate += tariff.getRate();
            }
            if (tariff.getType() == Tariff.Type.IMPORTS && type == Type.BUY) {
                rate += tariff.getRate();
            }
        }
    
        for (Tariff tariff : matchedTariffs) {
            if (tariff.getType() == Tariff.Type.IMPORTS && type == Type.SELL) {
                rate += tariff.getRate();
            }
            if (tariff.getType() == Tariff.Type.EXPORTS && type == Type.BUY) {
                rate += tariff.getRate();
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
    
    public enum Type {
        BUY, SELL
    }
}
