package net.pixlies.nations.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.nations.Nations;
import net.pixlies.nations.config.Config;
import org.bson.Document;

@Getter
public class MongoManager {

    private static final Nations instance = Nations.getInstance();
    private static final Main pixlies = Main.getInstance();

    private final Config config = instance.getConfig();
    private final MongoDatabase database = pixlies.getDatabase().getDatabase();

    private final MongoCollection<Document> nationCollection = database.getCollection(config.getString("network.database", "nations"));

}
