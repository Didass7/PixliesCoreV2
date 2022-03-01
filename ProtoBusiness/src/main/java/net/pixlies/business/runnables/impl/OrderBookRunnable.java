package net.pixlies.business.runnables.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.market.Order;
import net.pixlies.business.market.OrderBook;
import net.pixlies.core.runnables.PixliesRunnable;

import java.util.Map;

public class OrderBookRunnable extends PixliesRunnable {

    private final ProtoBusiness instance = ProtoBusiness.getInstance();

    public OrderBookRunnable() {
        super(true, 10L, 10L); // Refreshes every half second
    }

    @Override
    public void run() {
        Map<String, OrderBook> books = instance.getOrderManager().getBooks();

        // Runs for every book
        for (OrderBook book : books.values()) {
            try {
                Order order = book.getQueue().take();
                Order.OrderType type = order.getOrderType();

                switch (type) {
                    case BUY -> book.buy(order);
                    case SELL -> book.sell(order);
                    case CANCEL -> book.remove(order.getOrderId());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
