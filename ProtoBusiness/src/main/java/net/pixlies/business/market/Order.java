package net.pixlies.business.market;

import dev.morphia.annotations.*;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Orders
 *
 * @author vPrototype_
 */
@Getter
@Entity("orders")
@Indexes(
        @Index(fields = { @Field("orderId") })
)
public class Order {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Id private final @Getter String orderId;
    private final int timestamp;

    private final OrderType orderType;
    private final boolean limitOrder;

    private final UUID playerUUID;
    private final double price;
    private final int amount;
    private int volume;

    private final @Getter List<Trade> trades;

    public Order(OrderType type, int timestamp, boolean limitOrder, UUID uuid, double price, int amount) {
        orderId = TextUtils.generateId(7);
        orderType = type;
        this.timestamp = timestamp;
        this.limitOrder = limitOrder;
        playerUUID = uuid;
        this.price = price;
        this.amount = amount;
        volume = amount;
        trades = new LinkedList<>();
    }

    public void increaseVolume(int amount) {
        volume += amount;
    }

    public void decreaseVolume(int amount) {
        volume -= amount;
    }

    @Override
    public String toString() {
        if (limitOrder) return "t: " + timestamp + " | amount" + " @ " + price + "$ each (limit) | by " +
                Bukkit.getOfflinePlayer(playerUUID).getName();
        else return "t: " + timestamp + " | amount" + " @ " + price + "$ each | by " +
                Bukkit.getOfflinePlayer(playerUUID).getName();
    }

    public enum OrderType {
        BUY, SELL, CANCEL
    }

}
