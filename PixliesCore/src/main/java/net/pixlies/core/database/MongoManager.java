package net.pixlies.core.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import org.bson.Document;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoManager {

    private static final Main instance = Main.getInstance();

    private MongoClient client;

    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> punishmentCollection;
    private MongoCollection<Document> otherDocumentCollection;

    private final Map<UUID, User> userCache = new HashMap<>();
    private final List<String> bannedIps = new ArrayList<>();

    public MongoManager init() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        instance.getLogger().info("Connecting to MongoDB...");

        MongoCredential credential = MongoCredential.createCredential(
                conf("database.user", "user"),
                conf("database.database", "database"),
                conf("database.password", "").toCharArray()
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress(conf("database.host", "localhost"), Integer.parseInt(conf("database.port", "27017"))))))
                .build();

        client = MongoClients.create(settings);

        database = client.getDatabase(conf("database.database", "database"));

        // COLLECTIONS
        usersCollection = database.getCollection(conf("database.usersCollection", "users"));
        punishmentCollection = database.getCollection(conf("database.punishmentCollection", "punishments"));
        otherDocumentCollection = database.getCollection(conf("database.otherDocumentCollection", "otherDocuments"));

        reloadBannedIpList();

        instance.getLogger().info("Initialized MongoDB database.");
        return this;
    }

    public void reloadBannedIpList() {
        Document document = otherDocumentCollection.find(Filters.eq("identifier", "bannedIpList")).first();

        if (document == null) {
            saveBannedIpList();
            return;
        }

        bannedIps.clear();
        bannedIps.addAll(document.get("ips", new ArrayList<>()));
    }

    public void saveBannedIpList() {
        Document document = new Document();

        document.put("identifier", "bannedIpList");
        document.put("ips", bannedIps);

        if (otherDocumentCollection.find(Filters.eq("identifier", "bannedIpList")).first() == null) {
            otherDocumentCollection.insertOne(document);
            return;
        }
        otherDocumentCollection.replaceOne(Filters.eq("identifier", "bannedIpList"), document);
    }

    private static String conf(String what, String def) {
        return instance.getConfig().getString(what, def);
    }

}