package net.pixlies.business.market.orders;

import dev.morphia.annotations.*;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
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
        buyOrders.add(order);
        if (order.isLimitOrder()) processLimitOrder(order, sellOrders);
        else processMarketOrder(order, sellOrders);
    }

    public void sell(Order order) {
        sellOrders.add(order);
        if (order.isLimitOrder()) processLimitOrder(order, buyOrders);
        else processMarketOrder(order, buyOrders);
    }

    private void processMarketOrder(Order initialOrder, List<Order> orders) {
        for (Order oppositeOrder : orders) {
            // Check if the order has been filled already
            if (initialOrder.getVolume() == 0) break;

            // Check if the price matches
            if (oppositeOrder.getPrice() == initialOrder.getPrice()) {
                int volumeToDecrease = Math.min(initialOrder.getVolume(), oppositeOrder.getVolume());
                initialOrder.decreaseVolume(volumeToDecrease);
                oppositeOrder.decreaseVolume(volumeToDecrease);
            }
        }

        cleanUp();
        save();
    }

    private void processLimitOrder(Order initialOrder, List<Order> orders) {
        Order.OrderType type = initialOrder.getOrderType();
        for (Order oppositeOrder : orders) {
            // Check if the order has been filled already
            if (initialOrder.getVolume() == 0) break;

            boolean buyCondition = type == Order.OrderType.BUY && oppositeOrder.getPrice() <= initialOrder.getPrice();
            boolean sellCondition = type == Order.OrderType.SELL && oppositeOrder.getPrice() >= initialOrder.getPrice();

            // Check if the price matches
            if (buyCondition || sellCondition) {
                int volumeToDecrease = Math.min(initialOrder.getVolume(), oppositeOrder.getVolume());
                initialOrder.decreaseVolume(volumeToDecrease);
                oppositeOrder.decreaseVolume(volumeToDecrease);
            }
        }

        cleanUp();
        save();
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
