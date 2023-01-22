package net.pixlies.business.threads;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import net.pixlies.business.PixliesEconomy;
import net.pixlies.nations.Nations;
import org.bson.Document;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BalTop thread.
 *
 * @author MickMMars
 * @author vyketype
 */
public class BalTopThread extends Thread {
      public static final PixliesEconomy instance = PixliesEconomy.getInstance();
      public static final Nations nations = Nations.getInstance();
      
      public static final Map<String, Double> BALTOP_PLAYERS_MAP = new LinkedHashMap<>();
      public static final Map<String, Double> BALTOP_NATIONS_MAP = new LinkedHashMap<>();
      public static Date BALTOP_PLAYERS_DATE;
      public static Date BALTOP_NATIONS_DATE;
      
      public boolean running = false;
      
      // Updates every 4 minutes
      public void run() {
            while (running) {
                  try {
                        tickProfiles();
                        tickNations();
                        sleep(1000 * 60 * 4);
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
            }
      }
      
      public void tickProfiles() {
            instance.logInfo("Updating BalTop for user profiles...");
            BALTOP_PLAYERS_MAP.clear();
            
            MongoCollection<Document> nationProfiles = nations.getMongoManager().getNationProfileCollection();
            FindIterable<Document> cursor = nationProfiles.find().sort(Sorts.descending("balance")).limit(10);
            
            for (Document document : cursor) {
                  if (!document.containsKey("balance") || !document.containsKey("uuid"))
                        continue;
                  BALTOP_PLAYERS_MAP.put(document.getString("uuid"), document.getDouble("balance"));
            }
      
            BALTOP_PLAYERS_DATE = new Date();
      }
      
      public void tickNations() {
            instance.logInfo("Updating BalTop for nations...");
            BALTOP_NATIONS_MAP.clear();
      
            MongoCollection<Document> nationProfiles = nations.getMongoManager().getNationsCollection();
            FindIterable<Document> cursor = nationProfiles.find().sort(Sorts.descending("balance")).limit(10);
      
            for (Document document : cursor) {
                  if (!document.containsKey("balance") || !document.containsKey("nationId"))
                        continue;
                  BALTOP_NATIONS_MAP.put(document.getString("nationId"), document.getDouble("balance"));
            }
      
            BALTOP_NATIONS_DATE = new Date();
      }
      
      public void startThread() {
            this.running = true;
            this.start();
      }
      
      public void stopThread() {
            this.running = false;
      }
}
