package net.pixlies.business.market.orders;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.utils.TextUtils;

/**
 * Tariff class
 *
 * @author vPrototype_
 */
@Getter
@Entity("tariffs")
@Indexes(
        @Index(fields = { @Field("tariffId") })
)
public class Tariff {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    public Tariff(String fromId, String toId, double rate) {
        tariffId = TextUtils.generateId(7);
        from = fromId;
        to = toId;
        this.rate = rate;
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
        instance.getMongoManager().getDatastore().save(this);
    }

}
