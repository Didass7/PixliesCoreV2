package net.pixlies.business.market.orders;

import dev.morphia.annotations.*;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    public double getLowestBuyPrice() {
        List<Double> prices = new ArrayList<>();
        for (Order order : buyOrders) {
            prices.add(order.getPrice());
        }
        if (prices.isEmpty()) return 0;
        else return Collections.min(prices);
    }

    public double getHighestSellPrice() {
        List<Double> prices = new ArrayList<>();
        for (Order order : sellOrders) {
            prices.add(order.getPrice());
        }
        if (prices.isEmpty()) return 0;
        else return Collections.max(prices);
    }

    public void buy(Order order) {
        User user = User.get(order.getPlayerUUID());
        user.getStats().addBuy();
        user.save();

        instance.getStats().set("market.buyOrders", instance.getStats().getInt("market.buyOrders") + 1);

        buyOrders.add(order);
        if (order.isLimitOrder()) processLimitOrder(order, sellOrders);
        else processMarketOrder(order, sellOrders);
    }

    public void sell(Order order) {
        User user = User.get(order.getPlayerUUID());
        user.getStats().addSell();
        user.save();

        instance.getStats().set("market.sellOrders", instance.getStats().getInt("market.sellOrders") + 1);

        sellOrders.add(order);
        if (order.isLimitOrder()) processLimitOrder(order, buyOrders);
        else processMarketOrder(order, buyOrders);
    }

    private void processMarketOrder(Order initialOrder, List<Order> orders) {
        for (Order matchingOrder : orders) {
            // Check if the order has been filled already
            if (initialOrder.getVolume() == 0) break;

            // Check if the price matches
            if (matchingOrder.getPrice() == initialOrder.getPrice()) {
                int traded = Math.min(initialOrder.getVolume(), matchingOrder.getVolume());
                initialOrder.decreaseVolume(traded);
                matchingOrder.decreaseVolume(traded);
                addTrade(initialOrder, matchingOrder, traded);
            }
        }

        cleanUp();
        save();
    }

    private void processLimitOrder(Order initialOrder, List<Order> orders) {
        Order.OrderType type = initialOrder.getOrderType();
        for (Order matchingOrder : orders) {
            // Check if the order has been filled already
            if (initialOrder.getVolume() == 0) break;

            boolean buyCondition = type == Order.OrderType.BUY && matchingOrder.getPrice() <= initialOrder.getPrice();
            boolean sellCondition = type == Order.OrderType.SELL && matchingOrder.getPrice() >= initialOrder.getPrice();

            // Check if the price matches
            if (buyCondition || sellCondition) {
                int traded = Math.min(initialOrder.getVolume(), matchingOrder.getVolume());
                initialOrder.decreaseVolume(traded);
                matchingOrder.decreaseVolume(traded);
                addTrade(initialOrder, matchingOrder, traded);
            }
        }

        cleanUp();
        save();
    }

    private void addTrade(Order initialOrder, Order matchingOrder, int traded) {
        Order.OrderType type = initialOrder.getOrderType();
        double price = matchingOrder.getPrice() * traded;

        instance.getStats().set("market.trades", instance.getStats().getInt("market.trades") + 1);

        User initial = User.get(initialOrder.getPlayerUUID());
        User match = User.get(matchingOrder.getPlayerUUID());
        initial.getStats().addTrade();
        match.getStats().addTrade();

        Trade trade = null;
        switch (type) {
            case BUY -> {
                trade = new Trade(System.currentTimeMillis(), matchingOrder.getPrice(), traded, null, null, initialOrder.getPlayerUUID(), matchingOrder.getPlayerUUID(), false);
                match.getStats().addMoneyGained(price);
                match.getStats().addItemsSold(traded);
                initial.getStats().addMoneySpent(price);
                initial.getStats().addItemsBought(traded);
            }
            case SELL -> {
                trade = new Trade(System.currentTimeMillis(), matchingOrder.getPrice(), traded, initialOrder.getPlayerUUID(), matchingOrder.getPlayerUUID(), null, null, false);
                initial.getStats().addMoneyGained(price);
                initial.getStats().addItemsSold(traded);
                match.getStats().addMoneySpent(price);
                match.getStats().addItemsBought(traded);
            }
        }

        instance.getStats().set("market.moneyTraded", instance.getStats().getInt("market.moneyTraded") + price);
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
