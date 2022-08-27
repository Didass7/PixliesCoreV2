package net.pixlies.business.market.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;

/**
 * Trade class
 *
 * @author vPrototype_
 * @author NeedlessMemeing
 */
@Getter
@AllArgsConstructor
public class Trade {

    private long timestamp;
    private double price;
    private int amount;

    private UUID provider; // for sell orders
    private UUID taker; // for sell orders
    private UUID buyer; // for buy orders
    private UUID seller; // for buy orders

    private boolean claimed;

    public Trade(Document document) {
        this(
                document.getLong("timestamp"),
                document.getDouble("price"),
                document.getInteger("amount"),
                UUID.fromString(document.getString("provider")),
                UUID.fromString(document.getString("taker")),
                UUID.fromString(document.getString("buyer")),
                UUID.fromString(document.getString("seller")),
                document.getBoolean("claimed")
        );
    }

    public Document toDocument() {
        Document document = new Document();

        document.put("timestamp", timestamp);
        document.put("price", price);
        document.put("amount", amount);
        document.put("provider", provider.toString());
        document.put("taker", taker.toString());
        document.put("buyer", buyer.toString());
        document.put("seller", seller.toString());
        document.put("claimed", claimed);

        return document;
    }

    public void claim() {
        claimed = true;
    }

    public String toString(double originalPrice) {
        // TIMESTAMP
        long secondsTime = (System.currentTimeMillis() - timestamp) / 1000;
        String timestamp = secondsTime + "s";
        if (secondsTime > 60) timestamp = Math.round(secondsTime / 60.0) + "m";

        // BUY ORDERS
        if (provider == null) {
            String name = Objects.requireNonNull(Bukkit.getPlayer(seller)).getName();
            return "§8- §a" + amount + "§7x §f" + name + " §8" + timestamp + " ago";
        }

        // SELL ORDERS
        if (buyer == null) {
            String name = Objects.requireNonNull(Bukkit.getPlayer(taker)).getName();
            if (price == originalPrice) {
                return "§8- §a" + amount + "§7x §f" + name + " §8" + timestamp + " ago";
            } else {
                double tariff = (price / originalPrice - 1) * 100;
                return "§8- §a" + amount + "§7x §f" + name + " §8" + timestamp + " ago §7(tariffed §d" + "%§7)";
            }
        }

        return null;
    }

}
