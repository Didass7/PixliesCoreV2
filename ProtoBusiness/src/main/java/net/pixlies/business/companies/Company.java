package net.pixlies.business.companies;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;

import java.util.List;

/**
 * Company class ready to be put in MongoDB because of @Entity
 *
 * @author vPrototype_
 */
@Entity("companies")
public class Company {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private static final CompanyManager companyManager = new CompanyManager();

    @Id private @Getter @Setter String companyId;
    private String leaderUUID;
    private List<String> memberUUIDs;

    @Getter @Setter boolean publiclyTraded;

    public void save() {
        instance.getCompanyManager().getCompanies().put(companyId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

    public static Company getFromId(String id) {
        return companyManager.getCompanies().get(id);
    }


}
