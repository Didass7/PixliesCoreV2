package net.pixlies.proxy.database;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.entity.ProxyProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Some MongoDB and Morphia thing.
 * @author Dynmie
 */
public class MongoManager {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private final Logger logger = instance.getLogger();
    private final Config config = instance.getConfig();

    private @Getter MongoClient client;
    private @Getter Datastore datastore;
    private final @Getter Map<UUID, ProxyProfile> profileCache = new HashMap<>();

    public MongoManager() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
    }

    public void init() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        instance.getLogger().info("Connecting to MongoDB...");

        MongoCredential credential = MongoCredential.createCredential(conf("mongo.user"), conf("mongo.database"), conf("mongo.password").toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress(conf("mongo.host"), Integer.parseInt(conf("mongo.port"))))))
                .build();

        client = MongoClients.create(settings);

        datastore = Morphia.createDatastore(client, conf("mongo.database"), MapperOptions.builder()
                .storeEmpties(true)
                .build());
        datastore.ensureIndexes();

        datastore.getMapper().map(ProxyProfile.class);

        instance.getLogger().info("Connected to MongoDB database.");
    }

    private String conf(String what) {
        return config.getConfig().getString(what);
    }

}
