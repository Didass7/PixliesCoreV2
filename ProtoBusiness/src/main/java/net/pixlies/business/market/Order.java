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
    private @Getter @Setter double price;
    private @Getter @Setter int amount;
    private @Getter @Setter int volume;

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
        if (limitOrder) return "t: " + timestamp + " | amount" + " @ " + price + "$ each (limit) | by " +
                Bukkit.getOfflinePlayer(playerUUID).getName();
        else return "t: " + timestamp + " | amount" + " @ " + price + "$ each | by " +
                Bukkit.getOfflinePlayer(playerUUID).getName();
    }

    public enum OrderType {
        BUY, SELL, CANCEL
    }

}
