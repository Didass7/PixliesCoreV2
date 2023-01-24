package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.PixliesEconomy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents the order book for one item.
 *
 * @author vyketype
 */
@Getter
@AllArgsConstructor
public class OrderBook {
    private static final PixliesEconomy instance = PixliesEconomy.getInstance();
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
    
    public String getItemName() {
        return item.getName();
    }
    
    public List<String> getRecentOrders(UUID initialUUID) {
        List<String> list = new ArrayList<>();
        
        int buyIndex = 0;
        for (Order order : buyOrders) {
            if (buyIndex == 4) break;
            list.add(order.toString(initialUUID));
            buyIndex++;
        }
        
        int sellIndex = 0;
        for (Order order : sellOrders) {
            if (sellIndex == 4) break;
            list.add(order.toString(initialUUID));
            sellIndex++;
        }
        
        return list;
    }
    
    public double getLowestBuyPrice(UUID matching) {
        List<Double> prices = new ArrayList<>();
        buyOrders.forEach(order -> prices.add(order.getTaxedTariffedPrice(matching)));
        if (prices.isEmpty()) return 0;
        else return Collections.min(prices);
    }
    
    public double getHighestSellPrice(UUID matching) {
        List<Double> prices = new ArrayList<>();
        sellOrders.forEach(order -> prices.add(order.getTaxedTariffedPrice(matching)));
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
            if (initialOrder.getPlayerUUID() == matchingOrder.getPlayerUUID())
                continue;
            
            // Get relative prices
            double initialPrice = initialOrder.getTaxedTariffedPrice(matchingOrder.getPlayerUUID());
            double matchingPrice = matchingOrder.getTaxedTariffedPrice(initialOrder.getPlayerUUID());
            
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
                if (traded == 0)
                    continue;
                
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
        double price = matchingOrder.getTaxedTariffedPrice(initialOrder.getPlayerUUID());
        double total = price * traded;
        
        // Refunds
        double refund;
        if (type == Order.Type.BUY) {
            refund = initialOrder.getPrice() - initialOrder.getTariffedPrice(matchingOrder.getPlayerUUID());
            if (refund != 0) {
                initialOrder.getRefunds().put(refund, false);
                initialOrder.save();
            }
        } else {
            refund = matchingOrder.getPrice() - matchingOrder.getTariffedPrice(initialOrder.getPlayerUUID());
            if (refund != 0) {
                matchingOrder.getRefunds().put(refund, false);
                matchingOrder.save();
            }
        }
        
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
        buyOrders.removeIf(order -> order.getVolume() == 0 && order.isCancellable());
        sellOrders.removeIf(order -> order.getVolume() == 0 && order.isCancellable());
    }
    
    public void remove(Order order) {
        if (order.getType() == Order.Type.BUY) buyOrders.remove(order);
        else sellOrders.remove(order);
        save();
    }
    
    public void save() {
        CACHE.put(item.name(), this);
        instance.logInfo("Saved OrderBook of item " + getItemName() + " to the CACHE.");
    }
    
    public void backup() {
        String filename = item.name() + ".yml";
        File file = new File(BOOKS_PATH + filename);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        
        for (String key : yaml.getKeys(false)) {
            yaml.set(key, null);
        }
        
        try {
            yaml.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            instance.getLogger().log(Level.SEVERE, "This is an issue.");
        }
        
        for (Order order : buyOrders) {
            writeInFile(filename, order, "buys");
        }
        for (Order order : sellOrders) {
            writeInFile(filename, order, "sells");
        }
    
        instance.logInfo("Backed up OrderBook of item " + getItemName() + " to the files.");
    }
    
    private void writeInFile(String filename, Order order, String type) {
        File file = new File(BOOKS_PATH + filename);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        
        String initPath = type + "." + order.getOrderId() + ".";
    
        yaml.set(initPath + "timestamp", order.getTimestamp());
        yaml.set(initPath + "type", order.getType().name());
        yaml.set(initPath + "playerUUID", order.getPlayerUUID().toString());
        yaml.set(initPath + "price", order.getPrice());
        yaml.set(initPath + "amount", order.getAmount());
        yaml.set(initPath + "volume", order.getVolume());
        
        List<String> trades = new ArrayList<>() {{
            order.getTrades().forEach(trade -> add(trade.getSerializedString()));
        }};
        yaml.set(initPath + "trades", trades);
        
        for (Map.Entry<Double, Boolean> entry : order.getRefunds().entrySet()) {
            yaml.set(initPath + "refunds." + entry.getKey().toString(), entry.getValue());
        }
    
        try {
            yaml.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            instance.getLogger().log(Level.SEVERE, "Unable to save OrderBook of " + getItemName() + ".");
        }
    }
    
    // --------------------------------------------------------------------------------------------
    
    public static void loadAll() {
        for (OrderItem item : OrderItem.values()) {
            String filename = item.name() + ".yml";
            
            File file = new File(BOOKS_PATH + filename);
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            
            List<Order> buyOrders = new ArrayList<>();
            ConfigurationSection buys = yaml.getConfigurationSection("buys");
            if (buys != null) {
                for (String key : buys.getKeys(false)) {
                    buyOrders.add(getFromFile(filename, key, "buys", item.name()));
                }
            }
    
            List<Order> sellOrders = new ArrayList<>();
            ConfigurationSection sells = yaml.getConfigurationSection("sells");
            if (sells != null) {
                for (String key : sells.getKeys(false)) {
                    sellOrders.add(getFromFile(filename, key, "sells", item.name()));
                }
            }
            
            OrderBook book = new OrderBook(item, buyOrders, sellOrders);
            CACHE.put(item.name(), book);
    
            try {
                yaml.save(file);
            } catch (IOException ex) {
                ex.printStackTrace();
                instance.getLogger().log(Level.SEVERE, "Unable to save OrderBook of " + book.getItemName() + ".");
            }
        }
    
        instance.logInfo("All OrderBooks (" + CACHE.values().size() + ") have been loaded.");
    }
    
    private static Order getFromFile(String filename, String orderId, String type, String bookItem) {
        File file = new File(BOOKS_PATH + filename);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        
        String initPath = type + "." + orderId + ".";
        
        long timestamp = yaml.getLong(initPath + "timestamp");
        Order.Type orderType = Order.Type.valueOf(yaml.getString(initPath + "type"));
        UUID playerUUID = UUID.fromString(Objects.requireNonNull(yaml.getString(initPath + "playerUUID")));
        double price = yaml.getDouble(initPath + "price");
        int amount = yaml.getInt(initPath + "amount");
        int volume = yaml.getInt(initPath + "volume");
    
        List<Trade> trades = new ArrayList<>();
        for (String strTrade : yaml.getStringList(initPath + "trades")) {
            trades.add(new Trade(orderId, strTrade));
        }
        
        ConfigurationSection refundsSection = yaml.getConfigurationSection(initPath + "refunds");
        Map<Double, Boolean> refunds = new HashMap<>();
        if (refundsSection != null) {
            for (String key : refundsSection.getKeys(false)) {
                refunds.put(Double.parseDouble(key), yaml.getBoolean(initPath + "refunds." + key));
            }
        }
    
        return new Order(bookItem, orderId, timestamp, orderType, playerUUID, price, amount, volume, trades, refunds);
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
        return CACHE.values().stream().toList();
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
    
        instance.logInfo("All OrderBooks (" + CACHE.values().size() + ") have been reset.");
    }
}
