package net.pixlies.proxy.entity;

import dev.morphia.annotations.*;
import dev.morphia.query.experimental.filters.Filters;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.proxy.PixliesProxy;

import java.util.UUID;

@Data
@AllArgsConstructor
@Entity("proxyProfiles")
@Indexes(
        @Index(fields = { @Field("uuid") })
)
public class ProxyProfile {

    private static final PixliesProxy instance = PixliesProxy.getInstance();

    private @Id String uuid;
    private long creationMillis;

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    public void save() {
        instance.getMongoManager().getProfileCache().put(getUniqueId(), this);
        backup();
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

    public static ProxyProfile get(UUID uuid) {
        if (!instance.getMongoManager().getProfileCache().containsKey(uuid)) return getFromDatabase(uuid);
        return instance.getMongoManager().getProfileCache().get(uuid);
    }

    private static ProxyProfile getFromDatabase(UUID uuid) {
        ProxyProfile profile = instance.getMongoManager().getDatastore().find(ProxyProfile.class).filter(Filters.gte("uuid", uuid.toString())).first();

        if (profile == null) {
            profile = new ProxyProfile(
                    uuid.toString(),
                    System.currentTimeMillis()
            );

            profile.save();
            instance.getLogger().info("ProxyProfile for &6" + uuid + "&b created in database.");
            return profile;

        }

        // null check
        ProxyProfile returnProfile = new ProxyProfile(
                profile.uuid,
                profile.creationMillis
        );

        returnProfile.save();
        return returnProfile;
    }

}
