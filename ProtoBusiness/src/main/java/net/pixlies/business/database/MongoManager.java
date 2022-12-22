package net.pixlies.business.database;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.Main;
import net.pixlies.core.modules.configuration.ModuleConfig;
import org.bson.Document;

@Getter
public class MongoManager {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private static final Main pixlies = Main.getInstance();
    
    private final ModuleConfig config = instance.getConfig();
    private MongoCollection<Document> orderBookCollection;
    private MongoCollection<Document> tariffCollection;
    
    public void init() {
        orderBookCollection = pixlies.getMongoManager().getDatabase().getCollection("orderbooks");
        tariffCollection = pixlies.getMongoManager().getDatabase().getCollection("tariffCollection");
    }
}
