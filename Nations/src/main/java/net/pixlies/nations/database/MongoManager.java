package net.pixlies.nations.database;

import dev.morphia.Datastore;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.Nation;

@Getter
public class MongoManager {

    private static final Nations instance = Nations.getInstance();
    private static final Main pixlies = Main.getInstance();

    private final ModuleConfig config = instance.getConfig();
    private Datastore datastore;

    public void init() {
        pixlies.getDatabase().getDatastore().getMapper().map(Nation.class);

        datastore = pixlies.getDatabase().getDatastore();
        datastore.ensureIndexes();
    }

}
