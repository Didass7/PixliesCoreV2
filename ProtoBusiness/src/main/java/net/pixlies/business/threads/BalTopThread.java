package net.pixlies.business.threads;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.nations.Nations;
import org.bson.Document;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * BalTop thread.
 *
 * @author MickMMars
 * @author vyketype
 */
public class BalTopThread extends Thread {
      public static final ProtoBusiness instance = ProtoBusiness.getInstance();
      public static final Nations nations = Nations.getInstance();
      
      public static final Map<UUID, Double> BALTOP_MAP = new LinkedHashMap<>();
      public static Date DATE;
      
      public boolean running = false;
      
      // Updates every 5 minutes
      public void run() {
            while (running) {
                  try {
                        tick();
                        sleep(1000 * 60 * 5);
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
            }
      }
      
      public void tick() {
            instance.logInfo("Updating BalTop...");
            BALTOP_MAP.clear();
            
            MongoCollection<Document> nationProfiles = nations.getMongoManager().getNationProfileCollection();
            FindIterable<Document> cursor = nationProfiles.find().sort(Sorts.descending("balance")).limit(10);
            
            for (Document document : cursor) {
                  if (!document.containsKey("balance") || !document.containsKey("uniqueId"))
                        continue;
                  BALTOP_MAP.put(UUID.fromString(document.getString("uuid")), document.getDouble("balance"));
            }
            
            DATE = new Date();
      }
      
      public void startThread() {
            this.running = true;
            this.start();
      }
      
      public void stopThread() {
            this.running = false;
      }
}
