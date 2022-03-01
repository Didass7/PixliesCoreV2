package net.pixlies.business.market;

import dev.morphia.annotations.Id;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.utils.TextUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Represents the order book for one item
 *
 * @author vPrototype_
 */
@Getter
public class OrderBook {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Id private final String bookId;

    private final List<Order> buyOrders;
    private final List<Order> sellOrders;

    private final BlockingQueue<Order> queue;

    public OrderBook(BlockingQueue<Order> queue) {
        bookId = TextUtils.generateId(7);
        buyOrders = new LinkedList<>();
        sellOrders = new LinkedList<>();
        this.queue = queue;
    }

    // --------------------------------------------------------------------------------------------

    public void buy() {

    }

    public void sell() {

    }

    // --------------------------------------------------------------------------------------------

    public void save() {
        instance.getOrderManager().getBooks().put(bookId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

}
