package net.pixlies.business.market.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.core.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
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
    private static final String BOOKS_PATH = instance.getDataFolder().getAbsolutePath() + "/orderbooks/";
    private static final Map<String, OrderBook> CACHE = new HashMap<>();
    
    private final OrderItem item;
    
    private final List<Order> buyOrders;
    private final List<Order> sellOrders;
    
    public OrderBook(OrderItem item) {
        this.item = item;
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
    }
    
    // --------------------------------------------------------------------------------------------
    
    public String getItemName() {
        return item.getName().toLowerCase();
    }
    
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
    
    /**
     * What we've come to be, what we were before.
     * Ի՞նչ եղանք հիմա, ի՞նչ էինք առաջ։
     */
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
        
        Trade trade = new Trade(item.name(), System.currentTimeMillis(), price, traded,
                initialOrder.getPlayerUUID(), matchingOrder.getPlayerUUID(), false);
        
        switch (type) {
            case BUY -> {
                match.addMoneyGained(total);
                match.addItemsSold(traded);
                initial.addMoneySpent(total);
                initial.addItemsBought(traded);
            }
            case SELL -> {
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
    
    public void save() {
        CACHE.put(item.name(), this);
    }
    
    public void backup() {
        String filename = item.name() + ".yml";
        for (Order order : buyOrders) {
            writeInFile(filename, order, "buys");
        }
        for (Order order : sellOrders) {
            writeInFile(filename, order, "sells");
        }
    }
    
    private void writeInFile(String filename, Order order, String type) {
        Config file = new Config(new File(BOOKS_PATH + filename), filename);
        String initPath = type + "." + order.getOrderId() + ".";
        
        file.set(initPath + "timestamp", order.getTimestamp());
        file.set(initPath + "type", order.getType().name());
        file.set(initPath + "playerUUID", order.getPlayerUUID().toString());
        file.set(initPath + "price", order.getPrice());
        file.set(initPath + "amount", order.getAmount());
        file.set(initPath + "volume", order.getVolume());
        
        List<String> trades = new ArrayList<>() {{
            order.getTrades().forEach(trade -> add(trade.getSerializedString()));
        }};
        file.set(initPath + "trades", trades);
        
        file.save();
    }
    
    // --------------------------------------------------------------------------------------------
    
    public static void loadAll() {
        for (OrderItem item : OrderItem.values()) {
            String filename = item.name() + ".yml";
            Config file = new Config(new File(BOOKS_PATH + filename), filename);
            
            List<Order> buyOrders = new ArrayList<>();
            ConfigurationSection buys = file.getConfigurationSection("buys");
            if (buys != null) {
                for (String key : buys.getKeys(false)) {
                    buyOrders.add(getFromFile(filename, key, "buys", item.name()));
                }
            }
    
            List<Order> sellOrders = new ArrayList<>();
            ConfigurationSection sells = file.getConfigurationSection("sells");
            if (sells != null) {
                for (String key : sells.getKeys(false)) {
                    sellOrders.add(getFromFile(filename, key, "sells", item.name()));
                }
            }
            
            CACHE.put(item.name(), new OrderBook(item, buyOrders, sellOrders));
        }
    }
    
    private static Order getFromFile(String filename, String orderId, String type, String bookItem) {
        Config file = new Config(new File(BOOKS_PATH + filename), filename);
        String initPath = type + "." + orderId + ".";
        
        long timestamp = file.getLong(initPath + "timestamp");
        Order.Type orderType = Order.Type.valueOf(file.getString(initPath + "type"));
        UUID playerUUID = UUID.fromString(Objects.requireNonNull(file.getString(initPath + "playerUUID")));
        double price = file.getDouble(initPath + "price");
        int amount = file.getInt(initPath + "amount");
        int volume = file.getInt(initPath + "volume");
    
        List<Trade> trades = new ArrayList<>();
        for (String strTrade : file.getStringList(initPath + "trades")) {
            trades.add(new Trade(orderId, strTrade));
        }
    
        return new Order(bookItem, orderId, timestamp, orderType, playerUUID, price, amount, volume, trades);
    }
    
    public static void backupAll() {
        CACHE.values().forEach(OrderBook::backup);
    }
    
    public static OrderBook get(String itemName) {
        return CACHE.get(itemName);
    }
    
    public static OrderBook get(OrderItem item) {
        return get(item.name());
    }
    
    public static List<OrderBook> getAll() {
        return (List<OrderBook>) CACHE.values();
    }
    
    public static void resetAll() {
        // Reset stats.yml
        instance.getStats().set("market.buyOrders", 0);
        instance.getStats().set("market.sellOrders", 0);
        instance.getStats().set("market.trades", 0);
        instance.getStats().set("market.moneyTraded", 0);
        instance.getStats().set("market.itemsTraded", 0);
        
        // Clear all orders
        for (OrderBook book : getAll()) {
            book.getBuyOrders().clear();
            book.getSellOrders().clear();
            book.save();
        }
    }
}
