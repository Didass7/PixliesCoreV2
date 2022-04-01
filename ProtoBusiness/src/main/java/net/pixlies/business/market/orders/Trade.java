package net.pixlies.business.market.orders;

import dev.morphia.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Trade class
 *
 * @author vPrototype_
 * @author NeedlessMemeing
 */
@Getter
@AllArgsConstructor
@Entity("trades")
@Indexes(
        @Index(fields = { @Field("timestamp") })
)
public class Trade {

    @Id private int timestamp;
    private double price;
    private int amount;

    private UUID provider; // for sell orders
    private UUID taker; // for sell orders
    private UUID buyer; // for buy orders
    private UUID seller; // for buy orders

    private boolean claimed;

    public void claim() {
        claimed = true;
    }

    @Override
    public String toString() {
        String string = "t: " + timestamp + " | amount" + " @ " + price + "$ each - ";
        if (provider == null && taker == null) return string.concat("buyer: " + Bukkit.getOfflinePlayer(buyer).getName() +
                ", seller: " + Bukkit.getOfflinePlayer(seller).getName());
        if (buyer == null && seller == null) return string.concat("provider: " + Bukkit.getOfflinePlayer(provider).getName() +
                ", taker: " + Bukkit.getOfflinePlayer(taker).getName());
        return "error";
    }

}
