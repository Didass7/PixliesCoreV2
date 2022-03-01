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
 * Order class ready to be put in MongoDB because of @Entity
 *
 * @author vPrototype_
 */
@Entity("nations")
public class Order {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Id private final @Getter String orderId;
    private final @Getter int timestamp;

    private @Getter @Setter OrderType orderType;
    private @Getter @Setter boolean limitOrder;

    private final @Getter UUID playerUUID;
    private @Getter @Setter double minPricePerItem;
    private @Getter @Setter double maxPricePerItem;
    private @Getter @Setter int amount;

    /**
     * Represents the filled parts of the order.
     * First argument represents the UUID of the player who filled the order.
     * Second argument represents the amount of items filled.
     */
    @Getter private final Map<UUID, Integer> orderFills;

    public Order(OrderType type, int timestamp, boolean limitOrder, UUID uuid, double min, double max, int amount) {
        orderId = TextUtils.generateId(7);
        orderType = type;
        this.timestamp = timestamp;
        this.limitOrder = limitOrder;
        playerUUID = uuid;
        minPricePerItem = min;
        maxPricePerItem = max;
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

    public enum OrderType {
        BUY, SELL, CANCEL
    }

}
