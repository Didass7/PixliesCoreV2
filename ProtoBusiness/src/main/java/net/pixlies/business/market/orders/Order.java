package net.pixlies.business.market.orders;

import dev.morphia.annotations.*;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.nations.interfaces.NationProfile;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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

    @Id private final String orderId;
    private final String bookId;
    private @Setter long timestamp;

    private final Type type;

    private final UUID playerUUID;
    private @Setter double price;
    private @Setter int amount;
    private int volume;

    private final List<Trade> trades;

    public Order(Type type, String bookId, long timestamp, UUID uuid, double price, int amount) {
        orderId = TextUtils.generateId(7);
        this.bookId = bookId;
        this.type = type;
        this.timestamp = timestamp;
        playerUUID = uuid;
        this.price = price;
        this.amount = amount;
        volume = amount;
        trades = new LinkedList<>();
    }

    public double getPrice(UUID matchingUUID) {
        if (playerUUID == matchingUUID) return price;

        String from = Objects.requireNonNull(NationProfile.get(User.get(playerUUID))).getNation().getNationsId();
        String to = Objects.requireNonNull(NationProfile.get(User.get(matchingUUID))).getNation().getNationsId();
        double rate = 0;

        // Checks the tariffs list and sees if there are any that match
        for (Tariff tariff : instance.getMarketManager().getTariffs().values()) {
            if (Objects.equals(tariff.getFrom(), from) && Objects.equals(tariff.getTo(), to)) {
                rate = tariff.getRate();
                break;
            }
        }

        return price * (1 + rate);
    }

    public boolean isCancellable() {
        for (Trade t : trades) {
            if (!t.isClaimed()) return true;
        }
        return false;
    }

    public void decreaseVolume(int amount) {
        volume -= amount;
    }

    @Override
    public String toString() {
        return "t: " + timestamp + " | amount" + " @ " + price + "$ each | by " +
                Bukkit.getOfflinePlayer(playerUUID).getName();
    }

    public enum Type {
        BUY, SELL
    }

}
