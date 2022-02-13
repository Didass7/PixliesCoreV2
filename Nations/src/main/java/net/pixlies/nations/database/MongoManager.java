package net.pixlies.nations.database;

import dev.morphia.Datastore;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.nations.Nations;
import net.pixlies.nations.config.Config;

@Getter
public class MongoManager {

    private static final Nations instance = Nations.getInstance();
    private static final Main pixlies = Main.getInstance();

    private final Config config = instance.getConfig();
    private Datastore datastore;

    public void init() {
        pixlies.getDatabase().getUserCollection().getMapper().mapPackage("net.pixlies.nations");

        datastore = pixlies.getDatabase().getUserCollection();
        datastore.ensureIndexes();
    }

}
