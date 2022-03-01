package net.pixlies.business.market;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;
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
    private @Getter @Setter double minPrice;
    private @Getter @Setter double maxPrice;
    private @Getter @Setter int amount;
    private @Getter @Setter int volume;

    private final @Getter List<Trade> trades;

    public Order(OrderType type, int timestamp, boolean limitOrder, UUID uuid, double min, double max, int amount) {
        orderId = TextUtils.generateId(7);
        orderType = type;
        this.timestamp = timestamp;
        this.limitOrder = limitOrder;
        playerUUID = uuid;
        minPrice = min;
        maxPrice = max;
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

    /*

    public void save() {
        instance.getOrderManager().getOrders().put(orderId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

     */

    @Override
    public String toString() {
        if (minPrice == maxPrice) return "t: " + timestamp + " | amount" + " @ " + minPrice +
                "$ each | by " + Bukkit.getOfflinePlayer(playerUUID).getName();
        else return "t: " + timestamp + " | amount" + " @ " + minPrice + " to " + maxPrice +
                "$ each | by " + Bukkit.getOfflinePlayer(playerUUID).getName();
    }

    public enum OrderType {
        BUY, SELL, CANCEL
    }

}
