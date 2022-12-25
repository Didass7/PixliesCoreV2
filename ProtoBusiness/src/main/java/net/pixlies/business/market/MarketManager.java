package net.pixlies.business.market;

import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.orders.Tariff;
import net.pixlies.nations.nations.Nation;
import org.bson.Document;

import java.util.*;

/**
 * Market manager.
 *
 * @author vyketype
 */
public class MarketManager {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);
    
    @Getter
    private final Map<String, OrderBook> books = new HashMap<>(); // ID, OrderBook
    @Getter
    private final Map<String, Tariff> tariffs = new HashMap<>(); // ID, Tariff
    
    public MarketManager() {
        // TODO: fix OrderBooks thing
        hardReset();
        // loadBooks();
        loadTariffs();
    }
    
    public void backupAll() {
        for (OrderBook book : books.values()) {
            book.backup();
        }
        for (Tariff tariff : tariffs.values()) {
            tariff.backup();
        }
    }
    
    public void hardReset() {
        instance.getMongoManager().getOrderBookCollection().drop();
        
        for (OrderItem item : OrderItem.values()) {
            OrderBook book = new OrderBook(item);
            books.put(book.getBookId(), book);
        }
    }
    
    public void loadBooks() {
        for (Document document : instance.getMongoManager().getOrderBookCollection().find()) {
            OrderBook book = new OrderBook(document);
            books.put(book.getBookId(), book);
        }
    }
    
    public void loadTariffs() {
        for (Document document : instance.getMongoManager().getTariffCollection().find()) {
            Tariff tariff = new Tariff(document);
            tariffs.put(tariff.getTariffId(), tariff);
        }
    }
    
    public void resetBooks() {
        // Reset stats.yml
        instance.getStats().set("market.buyOrders", 0);
        instance.getStats().set("market.sellOrders", 0);
        instance.getStats().set("market.trades", 0);
        instance.getStats().set("market.moneyTraded", 0);
        instance.getStats().set("market.itemsTraded", 0);
        
        // Clear all orders
        for (OrderBook book : books.values()) {
            book.getBuyOrders().clear();
            book.getSellOrders().clear();
            book.save();
        }
    }
    
    public List<Order> getPlayerBuyOrders(UUID uuid) {
        List<Order> list = new LinkedList<>();
        for (OrderBook book : books.values()) {
            if (book.getBuyOrders() != null) {
                for (Order order : book.getBuyOrders()) {
                    if (order.getPlayerUUID() == uuid) list.add(order);
                }
            }
        }
        return list;
    }
    
    public List<Order> getPlayerSellOrders(UUID uuid) {
        List<Order> list = new LinkedList<>();
        for (OrderBook book : books.values()) {
            if (book.getSellOrders() != null) {
                for (Order order : book.getSellOrders()) {
                    if (order.getPlayerUUID() == uuid) list.add(order);
                }
            }
        }
        return list;
    }
    
    public OrderBook getBook(OrderItem item) {
        for (OrderBook book : books.values()) {
            if (book.getItem() == item) return book;
        }
        return null;
    }
    
    /**
     * Gets the tariff ID from the two Nation names
     *
     * @param from Name of the nation applying the tariff
     * @param to   Name of the nation affected by the tariff
     */
    public String getTariffId(String from, String to) {
        for (Tariff t : tariffs.values()) {
            boolean fromCond = Objects.equals(t.getFrom(),
                    Objects.requireNonNull(Nation.getFromName(from)).getNationId());
            boolean toCond = Objects.equals(t.getTo(), Objects.requireNonNull(Nation.getFromName(to)).getNationId());
            if (fromCond && toCond) return t.getTariffId();
        }
        return null;
    }
}
