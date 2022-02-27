package net.pixlies.business.market;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Order Class ready to be put in MongoDB because of @Entity
 *
 * @author vPrototype_
 */
@Entity("nations")
public class Order {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Id @Getter private final String orderId;

    @Getter private OrderType orderType;
    @Getter private final UUID playerUUID;
    @Getter @Setter private double pricePerItem;
    @Getter private final Material item;
    @Getter private final int amount;

    /**
     * Represents the filled parts of the order.
     * First argument represents the UUID of the player who filled the order.
     * Second argument represents the amount of items filled.
     */
    @Getter private final Map<UUID, Integer> orderFills;

    public Order(OrderType type, UUID uuid, double price, Material item, int amount) {
        orderId = TextUtils.generateId(7);
        orderType = type;
        playerUUID = uuid;
        pricePerItem = price;
        this.item = item;
        this.amount = amount;
        orderFills = new HashMap<>();
    }

    public void save() {
        instance.getOrderManager().getOrders().put(orderId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

    public void partiallyFillOrder(UUID uuid, int amount) {
        orderFills.put(uuid, amount);
    }

    /**
     * Flips the order.
     * If it was a Buy Order, it becomes a Sell Order.
     * If it was a Sell Order, it becomes a Buy Order.
     *
     * TODO needs work
     */
    public void flipOrder() {
        if (orderType == OrderType.BUY) orderType = OrderType.SELL;
        else orderType = OrderType.BUY;
    }

    public enum OrderType {
        BUY,
        SELL
    }

}
