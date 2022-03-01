package net.pixlies.business.market;

import lombok.Getter;
import net.pixlies.business.ProtoBusiness;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the saving of orders.
 *
 * @author vPrototype_
 */
public class OrderManager {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Getter private final Map<String, OrderBook> books = new HashMap<>(); // ID, OrderBook

    public OrderManager() {
        loadAll();
    }

    public void backupAll() {
        for (OrderBook book : books.values()) {
            book.backup();
        }
    }

    public void loadAll() {
        for (OrderBook book : instance.getMongoManager().getDatastore().find(OrderBook.class).iterator().toList()) {
            if (book.getBookId() != null) {
                books.put(book.getBookId(), book);
            }
        }
    }

}
