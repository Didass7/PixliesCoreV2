package net.pixlies.business.market;

import dev.morphia.annotations.Id;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.utils.TextUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Represents the order book for one item
 *
 * @author vPrototype_
 */
@Getter
public class OrderBook {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    public static double change = 0.01;

    @Id private final String bookId;

    private List<Order> buyOrders;
    private List<Order> sellOrders;
    private Map<String, Order> orderCache;

    private BlockingQueue<Order> queue;

    public OrderBook(BlockingQueue<Order> queue) {
        bookId = TextUtils.generateId(7);
        buyOrders = new LinkedList<>();
        sellOrders = new LinkedList<>();
        orderCache = new HashMap<>();
        this.queue = queue;
    }

    // --------------------------------------------------------------------------------------------

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
            if (initialOrder.getVolume() == 0) break; // Check if the order has been filled already

            // Check if the price matches
            if (oppositeOrder.getPrice() == initialOrder.getPrice()) {
                int volumeToDecrease = Math.min(initialOrder.getVolume(), oppositeOrder.getVolume());
                initialOrder.decreaseVolume(volumeToDecrease);
                oppositeOrder.decreaseVolume(volumeToDecrease);
            }

        }

        cleanUp();
    }

    private void processLimitOrder(Order initialOrder, List<Order> orders) {
        Order.OrderType type = initialOrder.getOrderType();
        for (Order oppositeOrder : orders) {
            if (initialOrder.getVolume() == 0) break; // Check if the order has been filled already

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
    }

    private void cleanUp() {
        buyOrders.removeIf(order -> order.getVolume() == 0);
        sellOrders.removeIf(order -> order.getVolume() == 0);
    }

    public void remove(String orderId) {
        if (orderCache.containsKey(orderId)) {
            Order order = orderCache.remove(orderId);
            if (order.getOrderType() == Order.OrderType.BUY) {
                buyOrders.remove(order);
            } else if (order.getOrderType() == Order.OrderType.SELL) {
                sellOrders.remove(order);
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    public void save() {
        instance.getOrderManager().getBooks().put(bookId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

}
