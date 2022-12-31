package net.pixlies.business.market.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.ranks.Rank;
import org.bukkit.Bukkit;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Trade class.
 *
 * @author vyketype
 */
@Getter
@AllArgsConstructor
public class Trade {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    
    private String orderId;
    
    private long timestamp;
    private double price;
    private int amount;
    
    private UUID giver;
    private UUID taker;

    private boolean claimed;
    
    public Trade(String orderId, String serialized) {
        this.orderId = orderId;
        
        String[] parts = serialized.split(";");
        timestamp = Long.parseLong(parts[0]);
        price = Double.parseDouble(parts[1]);
        amount = Integer.parseInt(parts[2]);
        giver = UUID.fromString(parts[3]);
        taker = UUID.fromString(parts[4]);
        claimed = Boolean.parseBoolean(parts[5]);
    }
    
    public void claim() {
        claimed = true;
    }
    
    @Override
    public String toString() {
        long secondsTime = (System.currentTimeMillis() - timestamp) / 1000;
        PrettyTime prettyTime = new PrettyTime();
        String time = prettyTime.format(LocalDateTime.now().minusSeconds(secondsTime));
        
        Order order = Order.get(orderId);
        String name;
    
        assert order != null;
        if (order.getType() == Order.Type.BUY) {
            name = Rank.getRank(giver).getColor() + Objects.requireNonNull(Bukkit.getPlayer(giver)).getName();
        } else {
            name = Rank.getRank(taker).getColor() + Objects.requireNonNull(Bukkit.getPlayer(taker)).getName();
        }
        
        return "§8» §a" + amount + "§8x §7@ §6" + price + "$ §8- " + name + " §8" + time;
    }
    
    public void save() {
        Order order = Order.get(orderId);
        assert order != null;
        for (Trade trade : order.getTrades()) {
            if (trade.getTimestamp() != timestamp) continue;
            order.getTrades().set(order.getTrades().indexOf(trade), this);
            order.save();
            return;
        }
        order.getTrades().add(this);
        order.save();
    
        instance.getServer().getLogger().info("Saved trade of timestamp " + timestamp + " to the CACHE.");
    }
    
    public String getSerializedString() {
        return timestamp + ";" + price + ";" + amount + ";" + giver.toString() + ";" + taker.toString() + ";" + claimed;
    }
}
