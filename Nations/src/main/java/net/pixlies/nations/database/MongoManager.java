package net.pixlies.nations.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.NationProfile;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoManager {

    private static final Nations instance = Nations.getInstance();

    private final ModuleConfig config = instance.getConfig();
    private MongoDatabase database;
    private MongoCollection<Document> nationsCollection;
    private MongoCollection<Document> nationProfileCollection;
    private MongoClient client;

    private final Map<UUID, NationProfile> profileCache = new HashMap<>();

    public void init() {

        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        instance.getLogger().info("Connecting to MongoDB...");

        MongoCredential credential = MongoCredential.createCredential(
                conf("mongo.user", "user"),
                conf("mongo.database", "database"),
                conf("mongo.password", "").toCharArray()
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress(conf("mongo.host", "localhost"), Integer.parseInt(conf("mongo.port", "27017"))))))
                .build();

        client = MongoClients.create(settings);

        database = client.getDatabase(conf("mongo.database", "database"));
        nationsCollection = database.getCollection(conf("mongo.nationsCollection", "nations"));
        nationProfileCollection = database.getCollection(conf("mongo.nationProfileCollection", "nationProfiles"));

        instance.getLogger().info("Initialized MongoDB database.");

    }

    private static String conf(String what, String def) {
        return instance.getConfig().getString(what, def);
    }

}
