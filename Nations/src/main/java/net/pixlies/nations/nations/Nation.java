package net.pixlies.nations.nations;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.Main;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.customization.GovernmentType;
import net.pixlies.nations.nations.customization.Ideology;
import net.pixlies.nations.nations.customization.Religion;
import net.pixlies.nations.nations.ranks.NationRank;
import org.bson.Document;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Nation {

    private static final Nations instance = Nations.getInstance();
    private static final Main pixlies = Main.getInstance();

    private String id;
    private String name;
    private String description;
    private String leaderUUID;
    private long created;

    private double politicalPower;
    private double money;

    private GovernmentType govType;
    private Ideology ideology;
    private Religion religion;
    private List<Integer> constitutionValues;

    private List<String> stateIds;

    private Map<String, Map<String, Object>> ranks;

    private List<String> memberUUIDs;

    public Nation create() {
        ranks.put("leader", NationRank.leader().toMap());
        ranks.put("admin", NationRank.admin().toMap());
        ranks.put("member", NationRank.member().toMap());
        ranks.put("newbie", NationRank.newbie().toMap());

        save();
        return this;
    }

    public void save() {
        instance.getNationManager().getNations().put(id, this);
    }

    public void backup() {
        Document nation = new Document("id", id);
        Document found = pixlies.getDatabase().getNationCollection().find(nation).first();

        // INFO
        nation.append("name", name);
        nation.append("description", description);
        nation.append("leaderUUID", leaderUUID);
        nation.append("created", created);

        // DATA
        nation.append("politicalPower", politicalPower);
        nation.append("money", money);

        // CUSTOMIZATION
        nation.append("ideology", ideology.toString());
        nation.append("govType", govType.toString());
        nation.append("religion", religion.toString());
        nation.append("constitutionValues", constitutionValues);

        // STATES
        nation.append("stateIds", stateIds);

        // RANKS
        nation.append("ranks", ranks);

        // MEMBERS
        nation.append("memberUUIDs", memberUUIDs);

        if (found != null) {
            instance.getMongoManager().getNationCollection().replaceOne(found, nation);
        } else {
            instance.getMongoManager().getNationCollection().insertOne(nation);
        }

    }

    private void addMember(String uuid) {
        memberUUIDs.add(uuid);
    }

    private void removeMember(String uuid) {
        memberUUIDs.remove(uuid);
    }

    private void editConstitution(byte law, int option) {
        constitutionValues.set(law, option);
    }

    
}
