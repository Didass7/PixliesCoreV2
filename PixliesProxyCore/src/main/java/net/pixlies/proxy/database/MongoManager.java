package net.pixlies.proxy.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.pixlies.proxy.Proxy;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Some MongoDB and Morphia thing.
 * @author Dynmie
 */
public class MongoManager {

    private static final Proxy instance = Proxy.getInstance();
    private final Logger logger = instance.getLogger();
    private final Configuration config = instance.getConfig().getConfig();

    @Getter private MongoClient client;
    @Getter private Datastore userDatastore;

    public MongoManager() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
    }

    public void init() {

        String uri = config.getString("mongo.host", "mongodb://localhost:27017");
        String userDatabase = config.getString("mongo.user-database", "proxy-users");

        ConnectionString connection = new ConnectionString(uri);

        logger.info("Attempting Mongo connection...");
        client = MongoClients.create(connection);

        MapperOptions options = MapperOptions.DEFAULT;
        userDatastore = Morphia.createDatastore(client, userDatabase, options);
        userDatastore.ensureIndexes();

    }

}
