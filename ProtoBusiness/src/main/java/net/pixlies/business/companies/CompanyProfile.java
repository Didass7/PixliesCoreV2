package net.pixlies.business.companies;


import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.entity.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Data
@AllArgsConstructor
@Entity("companyProfiles")
@Indexes(
        @Index(fields = { @Field("uuid") })
)
public class CompanyProfile {

    // -------------------------------------------------------------------------------------------------
    //                                              DATA
    // -------------------------------------------------------------------------------------------------

    // Player
    private String uuid;

    // Companies
    private String companyId;
    private String companyRank;

    // -------------------------------------------------------------------------------------------------
    //                                            TODO: METHODS
    // -------------------------------------------------------------------------------------------------

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    // -------------------------------------------------------------------------------------------------
    //                                          STATIC METHODS
    // -------------------------------------------------------------------------------------------------

    public static boolean isInCompany(@NotNull User user) {
        return user.getExtras().containsKey("companyProfile");
    }

    public static @Nullable CompanyProfile get(@NotNull User user) {
        if (!isInCompany(user)) return null;
        return (CompanyProfile) user.getExtras().get("companyProfile");
    }

}
