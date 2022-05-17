package net.pixlies.business.market.orders;

import dev.morphia.annotations.*;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.TextUtils;

import java.util.*;

/**
 * Represents the order book for one item
 *
 * @author vPrototype_
 */
@Getter
@Entity("orderbooks")
@Indexes(
        @Index(fields = { @Field("bookId") })
)
public class OrderBook {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    public static double change = 0.01;

    @Id private final String bookId;
    private final OrderItem item;

    private final List<Order> buyOrders;
    private final List<Order> sellOrders;

    public OrderBook(OrderItem item) {
        this.item = item;
        bookId = TextUtils.generateId(7);
        buyOrders = new LinkedList<>();
        sellOrders = new LinkedList<>();
    }

    // --------------------------------------------------------------------------------------------

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
        User user = User.get(order.getPlayerUUID());
        user.getStats().addBuy();
        user.save();

        instance.getStats().set("market.buyOrders", instance.getStats().getInt("market.buyOrders") + 1);

        buyOrders.add(order);
        save();
        processOrder(order, sellOrders);
    }

    public void sell(Order order) {
        User user = User.get(order.getPlayerUUID());
        user.getStats().addSell();
        user.save();

        instance.getStats().set("market.sellOrders", instance.getStats().getInt("market.sellOrders") + 1);

        sellOrders.add(order);
        processOrder(order, buyOrders);
    }

    private void processOrder(Order initialOrder, List<Order> orders) {
        Order.OrderType type = initialOrder.getOrderType();
        for (Order matchingOrder : orders) {
            // Get relative prices
            double initialPrice = initialOrder.getPrice(matchingOrder.getPlayerUUID());
            double matchingPrice = matchingOrder.getPrice(initialOrder.getPlayerUUID());

            boolean buyCondition = type == Order.OrderType.BUY && matchingPrice <= initialPrice;
            boolean sellCondition = type == Order.OrderType.SELL && matchingPrice >= initialPrice;

            // Check if the price matches
            if (buyCondition || sellCondition) {
                int traded = Math.min(initialOrder.getVolume(), matchingOrder.getVolume());
                initialOrder.decreaseVolume(traded);
                matchingOrder.decreaseVolume(traded);
                addTrade(initialOrder, matchingOrder, traded);
            }
        }

        cleanUp();
    }

    private void addTrade(Order initialOrder, Order matchingOrder, int traded) {
        Order.OrderType type = initialOrder.getOrderType();
        double price = matchingOrder.getPrice(initialOrder.getPlayerUUID());
        double total = price * traded;

        instance.getStats().set("market.trades", instance.getStats().getInt("market.trades") + 1);

        User initial = User.get(initialOrder.getPlayerUUID());
        User match = User.get(matchingOrder.getPlayerUUID());
        initial.getStats().addTrade();
        match.getStats().addTrade();

        Trade trade = null;
        switch (type) {
            case BUY -> {
                trade = new Trade(System.currentTimeMillis(), price, traded, null, null, initialOrder.getPlayerUUID(), matchingOrder.getPlayerUUID(), false);
                match.getStats().addMoneyGained(total);
                match.getStats().addItemsSold(traded);
                initial.getStats().addMoneySpent(total);
                initial.getStats().addItemsBought(traded);
            }
            case SELL -> {
                trade = new Trade(System.currentTimeMillis(), price, traded, initialOrder.getPlayerUUID(), matchingOrder.getPlayerUUID(), null, null, false);
                initial.getStats().addMoneyGained(total);
                initial.getStats().addItemsSold(traded);
                match.getStats().addMoneySpent(total);
                match.getStats().addItemsBought(traded);
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
        if (order.getOrderType() == Order.OrderType.BUY) buyOrders.remove(order);
        else if (order.getOrderType() == Order.OrderType.SELL) sellOrders.remove(order);
        save();
    }

    // --------------------------------------------------------------------------------------------

    public void save() {
        instance.getMarketManager().getBooks().put(bookId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

}
