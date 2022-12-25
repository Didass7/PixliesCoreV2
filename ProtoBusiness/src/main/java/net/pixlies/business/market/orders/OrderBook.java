package net.pixlies.business.market.orders;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.core.utils.TextUtils;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.*;

/**
 * Represents the order book for one item.
 *
 * @author vyketype
 */
@Getter
@AllArgsConstructor
public class OrderBook {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    
    private final String bookId;
    private final OrderItem item;
    
    private final List<Order> buyOrders;
    private final List<Order> sellOrders;
    
    public OrderBook(OrderItem item) {
        this.item = item;
        bookId = TextUtils.generateId(32);
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
    }
    
    public OrderBook(Document document) {
        this(
                document.getString("bookId"),
                OrderItem.valueOf(document.getString("item")),
                new ArrayList<>() {{
                    for (Document orderDocument : document.getList("buyOrders", Document.class)) {
                        add(new Order(orderDocument));
                    }
                }},
                new ArrayList<>() {{
                    for (Document orderDocument : document.getList("sellOrders", Document.class)) {
                        add(new Order(orderDocument));
                    }
                }}
        );
    }
    
    // --------------------------------------------------------------------------------------------
    
    public List<String> getRecentOrders(Order.Type type, OrderItem item, UUID uuid) {
        List<String> list = new ArrayList<>();
        OrderBook book = item.getBook();
        assert book != null;
        
        List<Order> orders;
        if (type == Order.Type.BUY) orders = book.getBuyOrders();
        else orders = book.getSellOrders();
        
        for (Order o : orders) {
            if (list.size() == 8) break;
            String playerName = Objects.requireNonNull(Bukkit.getPlayer(o.getPlayerUUID())).getName();
            list.add("§8- §a" + o.getAmount() + "§8x§7 at §6" + o.getPrice(uuid) + "§7 each from §b" + playerName);
        }
        
        return list;
    }
    
    public double getLowestBuyPrice(UUID matching) {
        List<Double> prices = new ArrayList<>();
        buyOrders.forEach(order -> prices.add(order.getPrice(matching)));
        if (prices.isEmpty()) return 0;
        else return Collections.min(prices);
    }
    
    public double getHighestSellPrice(UUID matching) {
        List<Double> prices = new ArrayList<>();
        sellOrders.forEach(order -> prices.add(order.getPrice(matching)));
        if (prices.isEmpty()) return 0;
        else return Collections.max(prices);
    }
    
    public void buy(Order order) {
        MarketProfile profile = MarketProfile.get(order.getPlayerUUID());
        profile.addBuy();
        profile.save();
        
        instance.getStats().set("market.buyOrders", instance.getStats().getInt("market.buyOrders") + 1);
        
        buyOrders.add(order);
        save();
        processOrder(order, sellOrders);
    }
    
    public void sell(Order order) {
        MarketProfile profile = MarketProfile.get(order.getPlayerUUID());
        profile.addSell();
        profile.save();
        
        instance.getStats().set("market.sellOrders", instance.getStats().getInt("market.sellOrders") + 1);
        
        sellOrders.add(order);
        processOrder(order, buyOrders);
    }
    
    private void processOrder(Order initialOrder, List<Order> orders) {
        Order.Type type = initialOrder.getType();
        for (Order matchingOrder : orders) {
            // Get relative prices
            double initialPrice = initialOrder.getPrice(matchingOrder.getPlayerUUID());
            double matchingPrice = matchingOrder.getPrice(initialOrder.getPlayerUUID());
            
            boolean buyCondition = type == Order.Type.BUY && matchingPrice <= initialPrice;
            boolean sellCondition = type == Order.Type.SELL && matchingPrice >= initialPrice;
            
            MarketProfile initProfile = MarketProfile.get(initialOrder.getPlayerUUID());
            MarketProfile matchProfile = MarketProfile.get(matchingOrder.getPlayerUUID());
            if (initProfile.getBlockedPlayers().contains(matchingOrder.getPlayerUUID()))
                continue;
            if (matchProfile.getBlockedPlayers().contains(initialOrder.getPlayerUUID()))
                continue;
            
            // Check if the price matches
            if (buyCondition || sellCondition) {
                int traded = Math.min(initialOrder.getVolume(), matchingOrder.getVolume());
                initialOrder.decreaseVolume(traded);
                matchingOrder.decreaseVolume(traded);
                addTrade(initialOrder, matchingOrder, traded);
                
                initProfile.sendNotification();
                matchProfile.sendNotification();
            }
        }
        
        cleanUp();
    }
    
    private void addTrade(Order initialOrder, Order matchingOrder, int traded) {
        Order.Type type = initialOrder.getType();
        double price = matchingOrder.getPrice(initialOrder.getPlayerUUID());
        double total = price * traded;
        
        instance.getStats().set("market.trades", instance.getStats().getInt("market.trades") + 1);
        
        MarketProfile initial = MarketProfile.get(initialOrder.getPlayerUUID());
        MarketProfile match = MarketProfile.get(matchingOrder.getPlayerUUID());
        initial.addTrade();
        match.addTrade();
        
        Trade trade = null;
        switch (type) {
            case BUY -> {
                trade = new Trade(System.currentTimeMillis(), price, traded, null, null,
                        initialOrder.getPlayerUUID(), matchingOrder.getPlayerUUID(), false);
                match.addMoneyGained(total);
                match.addItemsSold(traded);
                initial.addMoneySpent(total);
                initial.addItemsBought(traded);
            }
            case SELL -> {
                trade = new Trade(System.currentTimeMillis(), price, traded, initialOrder.getPlayerUUID(),
                        matchingOrder.getPlayerUUID(), null, null, false);
                initial.addMoneyGained(total);
                initial.addItemsSold(traded);
                match.addMoneySpent(total);
                match.addItemsBought(traded);
            }
        }
        
        instance.getStats().set("market.moneyTraded", instance.getStats().getInt("market.moneyTraded") + total);
        instance.getStats().set("market.itemsTraded", instance.getStats().getInt("market.itemsTraded") + traded);
        
        initialOrder.getTrades().add(trade);
        matchingOrder.getTrades().add(trade);
        
        initial.save();
        match.save();
    }
    
    private void cleanUp() {
        buyOrders.removeIf(order -> order.getVolume() == 0);
        sellOrders.removeIf(order -> order.getVolume() == 0);
    }
    
    public void remove(Order order) {
        if (order.getType() == Order.Type.BUY) buyOrders.remove(order);
        else sellOrders.remove(order);
        save();
    }
    
    public Document toDocument() {
        Document document = new Document();
        
        document.put("bookId", bookId);
        document.put("item", item);
        document.put("buyOrders", new ArrayList<Document>() {{
            for (Order buyOrder : buyOrders) {
                add(buyOrder.toDocument());
            }
        }});
        document.put("sellOrders", new ArrayList<Document>() {{
            for (Order sellOrder : sellOrders) {
                add(sellOrder.toDocument());
            }
        }});
        
        return document;
    }
    
    // --------------------------------------------------------------------------------------------
    
    public void save() {
        instance.getMarketManager().getBooks().put(bookId, this);
        instance.getServer().getScheduler().runTaskAsynchronously(instance, this::backup);
    }
    
    public void backup() {
        if (instance.getMongoManager().getOrderBookCollection().find(Filters.eq("bookId", bookId)).first() == null) {
            instance.getMongoManager().getOrderBookCollection().insertOne(toDocument());
        }
        instance.getMongoManager().getOrderBookCollection().replaceOne(Filters.eq("bookId", bookId), toDocument());
    }
}
