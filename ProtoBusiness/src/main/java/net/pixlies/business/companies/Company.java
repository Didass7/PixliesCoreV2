package net.pixlies.business.companies;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Company class ready to be put in MongoDB because of @Entity
 *
 * @author vPrototype_
 */
@Entity("companies")
@Getter
public class Company {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private static final CompanyManager companyManager = new CompanyManager();

    @Id private final String companyId;
    private @Setter String name;
    private @Setter String description;
    private @Setter Industry industry;

    private long created;

    private String leaderUUID;
    private List<String> memberUUIDs;

    @Setter boolean publiclyTraded;

    public Company(String leaderUUID) {
        companyId = TextUtils.generateId(7);
        this.leaderUUID = leaderUUID;
        memberUUIDs = new ArrayList<>();
        publiclyTraded = false;
    }

    public void save() {
        instance.getCompanyManager().getCompanies().put(companyId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

}
