package net.pixlies.nations.nations;

import lombok.Data;
import net.pixlies.nations.nations.ranks.Rank;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class Nation implements NationsEntity {

    private String id;
    private String name;
    // UUID & Rank Name
    private Map<UUID, String> members;
    private Map<String, Map<String, Object>> ranks;

    public Nation create() {
        ranks.put("leader", Rank.LEADER().toMap());
        ranks.put("admin", Rank.ADMIN().toMap());
        ranks.put("member", Rank.MEMBER().toMap());
        ranks.put("newbie", Rank.NEWBIE().toMap());
        save();
        return this;
    }

    private void save() {
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }
    
}
