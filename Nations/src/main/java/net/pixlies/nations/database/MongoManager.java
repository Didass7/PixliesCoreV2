package net.pixlies.nations.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;

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
    private Datastore datastore;
    private MongoClient client;

    private final Map<UUID, NationProfile> profileCache = new HashMap<>();

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

        datastore.getMapper().map(
                Nation.class,
                NationProfile.class
        );

        datastore.ensureIndexes();

        instance.getLogger().info("Connected to MongoDB database.");

    }

    private static String conf(String what) {
        return instance.getConfig().getString(what);
    }

}
