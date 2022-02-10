package net.pixlies.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;
import net.pixlies.core.Main;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoManager {

    private static final Main instance = Main.getInstance();

    private MongoClient client;
    private Datastore datastore;
    private Morphia morphia;

    private final UserCache userCache = new UserCache();

    public MongoManager init() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        instance.getLogger().info("Connecting to MongoDB...");

        String uri = instance.getConfig().getString("database.uri");
        if (uri == null) {
            instance.getLogger().warning("Plugin can't start because MongoDB URI is missing.");
            Bukkit.getPluginManager().disablePlugin(instance);
            return this;
        }
        MongoClientURI clientURI = new MongoClientURI(uri);
        client = new MongoClient(clientURI);

        morphia = new Morphia();
        morphia.mapPackage("net.pixlies.core");

        datastore = morphia.createDatastore(client, instance.getConfig().getString("database.database", "admin"));

        datastore.ensureIndexes();

        instance.getLogger().info("Connected to MongoDB database.");
        return this;
    }

}
