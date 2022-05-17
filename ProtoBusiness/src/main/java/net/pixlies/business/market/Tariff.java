package net.pixlies.business.market;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;

/**
 * Tariff class
 *
 * @author vPrototype_
 */
@Getter
@AllArgsConstructor
@Entity("tariffs")
@Indexes(
        @Index(fields = { @Field("tariffId") })
)
public class Tariff {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    private final String tariffId;

    /**
     * UUID of the nation applying the tariff
     */
    private final String from;

    /**
     * UUID of the nation receiving the tariff
     */
    private final String to;

    private @Setter double rate;

    public void save() {
        instance.getMarketManager().getTariffs().put(tariffId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

}
