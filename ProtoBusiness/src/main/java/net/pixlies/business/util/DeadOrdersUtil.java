package net.pixlies.business.util;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.market.Order;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DeadOrdersUtil {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      private static final String DEAD_PATH = instance.getDataFolder().getAbsolutePath() + "/dead_orders/";
      
      public static void backupDeadOrder(Order order) {
            String filename = order.getOrderId() + ".yml";
            File file = new File(DEAD_PATH + filename);
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
      
            yaml.set("orderBook", order.getBookItem());
            yaml.set("timestamp", order.getTimestamp());
            yaml.set("type", order.getType().name());
            yaml.set("playerUUID", order.getPlayerUUID().toString());
            yaml.set("price", order.getPrice());
            yaml.set("amount", order.getAmount());
            yaml.set("volume", order.getVolume());
      
            List<String> trades = new ArrayList<>() {{
                  order.getTrades().forEach(trade -> add(trade.getSerializedString()));
            }};
            yaml.set("trades", trades);
      
            for (Map.Entry<BigDecimal, Boolean> entry : order.getRefunds().entrySet()) {
                  yaml.set("refunds." + entry.getKey().doubleValue(), entry.getValue());
            }
      
            try {
                  yaml.save(file);
            } catch (IOException ex) {
                  ex.printStackTrace();
                  instance.getLogger().log(Level.SEVERE, "Unable to save dead order " + order.getOrderId() + ".");
            }
      }
}
