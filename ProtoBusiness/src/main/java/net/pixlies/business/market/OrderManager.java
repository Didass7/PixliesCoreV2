package net.pixlies.business.market;

import lombok.Getter;
import net.pixlies.business.ProtoBusiness;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Getter private final Map<String, Order> orders = new HashMap<>(); // ID, Order

    public OrderManager() {
        loadAll();
    }

    public void backupAll() {
        for (Order order : orders.values()) {
            order.backup();
        }
    }

    public void loadAll() {
        for (Order order : instance.getMongoManager().getDatastore().find(Order.class).iterator().toList()) {
            if (order.getOrderId() != null) {
                orders.put(order.getOrderId(), order);
            }
        }
    }

}
