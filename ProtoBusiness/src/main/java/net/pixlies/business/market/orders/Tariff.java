package net.pixlies.business.market.orders;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.utils.TextUtils;
import org.bson.Document;

/**
 * Tariff class
 *
 * @author vyketype
 */
@Getter
@AllArgsConstructor
public class Tariff {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    public Tariff(String fromId, String toId, double rate) {
        tariffId = TextUtils.generateId(7);
        from = fromId;
        to = toId;
        this.rate = rate;
    }

    public Tariff(Document document) {
        this(
                document.getString("tariffId"),
                document.getString("from"),
                document.getString("to"),
                document.getDouble("rate")
        );
    }

    public Document toDocument() {
        Document document = new Document();

        document.put("tariffId", tariffId);
        document.put("from", from);
        document.put("to", to);
        document.put("rate", rate);

        return document;
    }

    private final String tariffId;

    /**
     * UUID of the nation applying the tariff
     */
    private final String from;

    /**
     * UUID of the nation receiving the tariff
     */
    private final String to;

    @Setter private double rate;

    public String getFormattedRate() {
        return (rate * 100) + "%";
    }

    public void save() {
        instance.getMarketManager().getTariffs().put(tariffId, this);
    }

    public void backup() {
        if (instance.getMongoManager().getOrderBookCollection().find(Filters.eq("tariffId", tariffId)).first() == null) {
            instance.getMongoManager().getOrderBookCollection().insertOne(toDocument());
        }
        instance.getMongoManager().getOrderBookCollection().replaceOne(Filters.eq("tariffId", tariffId), toDocument());
    }

}
