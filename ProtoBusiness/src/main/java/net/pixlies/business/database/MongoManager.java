package net.pixlies.business.database;

import dev.morphia.Datastore;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.companies.Company;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;

@Getter
public class MongoManager {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private static final Main pixlies = Main.getInstance();

    private final Config config = instance.getConfig();
    private Datastore datastore;

    public void init() {
        pixlies.getDatabase().getDatastore().getMapper().map(Company.class);

        datastore = pixlies.getDatabase().getDatastore();
        datastore.ensureIndexes();
    }

}
