package net.pixlies.nations.nations;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.nations.nations.ranks.Rank;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Nation {

    private String id;
    private String name;
    // UUID & Rank Name
    private Map<UUID, String> members;
    private Map<String, Map<String, Object>> ranks;

    public Nation create() {
        ranks.put("leader", Rank.leader().toMap());
        ranks.put("admin", Rank.admin().toMap());
        ranks.put("member", Rank.member().toMap());
        ranks.put("newbie", Rank.newbie().toMap());
        save();
        return this;
    }

    private void save() {

    }
    
}
