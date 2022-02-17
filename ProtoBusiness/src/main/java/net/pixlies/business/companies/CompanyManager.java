package net.pixlies.business.companies;

import lombok.Getter;
import net.pixlies.business.ProtoBusiness;

import java.util.HashMap;
import java.util.Map;

public class CompanyManager {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Getter
    private final Map<String, Company> companies = new HashMap<>(); // UUID/ID, Company

    public CompanyManager() {
        loadAll();
    }

    public void backupAll() {
        for (Company company : companies.values()) {
            company.backup();
        }
    }

    public void loadAll() {
        for (Company company : instance.getMongoManager().getDatastore().find(Company.class).iterator().toList()) {
            if (company.getCompanyId() != null) {
                companies.put(company.getCompanyId(), company);
            }
        }
    }

}
