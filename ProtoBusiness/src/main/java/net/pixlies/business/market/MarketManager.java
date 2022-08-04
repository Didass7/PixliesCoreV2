package net.pixlies.business.market;

import dev.morphia.query.Query;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.orders.Tariff;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.nations.Nation;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Market manager
 *
 * @author vPrototype_
 */
public class MarketManager {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);

    @Getter private final Map<String, OrderBook> books = new HashMap<>(); // ID, OrderBook
    @Getter private final Map<String, Tariff> tariffs = new HashMap<>(); // ID, Tariff

    public MarketManager() {
        // TODO: fix OrderBooks thing
        hardReset();
        // loadBooks();
        loadTariffs();
    }

    public void backupAll() {
        for (OrderBook book : books.values()) {
            instance.getMongoManager().getDatastore().save(book);
        }
        for (Tariff tariff : tariffs.values()) {
            tariff.backup();
        }
    }

    public void hardReset() {
        Query<OrderBook> query = instance.getMongoManager().getDatastore().find(OrderBook.class);
        query.delete();

        for (OrderItem item : OrderItem.values()) {
            OrderBook book = new OrderBook(item);
            books.put(book.getBookId(), book);
        }
    }

    public void loadBooks() {
        Query<OrderBook> query = instance.getMongoManager().getDatastore().find(OrderBook.class);
        for (OrderBook book : query.iterator().toList()) {
            books.put(book.getBookId(), book);
        }
    }

    public void loadTariffs() {
        Query<Tariff> query = instance.getMongoManager().getDatastore().find(Tariff.class);
        for (Tariff tariff : query.iterator().toList()) {
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

    public void resetPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        User user = User.get(uuid);

        // Reset player stats
        user.setBuyOrdersMade(0);
        user.setSellOrdersMade(0);
        user.setMoneySpent(0);
        user.setMoneyGained(0);
        user.setTradesMade(0);
        user.setItemsSold(0);
        user.setItemsBought(0);

        marketHandler.getChallenges().removeAll(uuid.toString());
        marketHandler.getNotifs().removeAll(uuid.toString());

        // Clear orders of that player
        for (OrderBook book : books.values()) {
            book.getBuyOrders().removeIf(order -> order.getPlayerUUID() == uuid);
            book.getSellOrders().removeIf(order -> order.getPlayerUUID() == uuid);
            book.save();
        }

        // TODO reset money

        user.save();
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
     * @param from Name of the nation applying the tariff
     * @param to Name of the nation affected by the tariff
     */
    public String getTariffId(String from, String to) {
        for (Tariff t : tariffs.values()) {
            boolean fromCond = Objects.equals(t.getFrom(), Objects.requireNonNull(Nation.getFromName(from)).getNationId());
            boolean toCond = Objects.equals(t.getTo(), Objects.requireNonNull(Nation.getFromName(to)).getNationId());
            if (fromCond && toCond) return t.getTariffId();
        }
        return null;
    }

}
