package net.pixlies.core.database;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.economy.Wallet;
import net.pixlies.core.entity.User;
import net.pixlies.core.entity.data.PermissionProfile;
import net.pixlies.core.entity.data.UserPersonalization;
import net.pixlies.core.entity.data.UserSettings;
import net.pixlies.core.moderation.Punishment;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoManager {

    private static final Main instance = Main.getInstance();

    private MongoClient client;
    private Datastore datastore;
    private final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(
                    PojoCodecProvider.builder()
                            .register(UserSettings.class)
                            .register(Wallet.class)
                            .register(UserPersonalization.class)
                            .register(PermissionProfile.class)
                            .register(Punishment.class)
                            .build()
            )
    );

    private final UserCache userCache = new UserCache();

    public MongoManager init() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        instance.getLogger().info("Connecting to MongoDB...");

        MongoCredential credential = MongoCredential.createCredential(conf("database.user"), conf("database.database"), conf("database.password").toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress(conf("database.host"), Integer.parseInt(conf("database.port"))))))
                .build();

        client = MongoClients.create(
                settings
        );

        datastore = Morphia.createDatastore(client, conf("database.database"));

        datastore.getMapper().map(User.class);

        instance.getLogger().info("Connected to MongoDB database.");
        return this;
    }

    private static String conf(String what) {
        return instance.getConfig().getString(what);
    }

}
