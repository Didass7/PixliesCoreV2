package net.pixlies.nations.nations;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.customization.GovernmentType;
import net.pixlies.nations.nations.customization.Ideology;
import net.pixlies.nations.nations.customization.Religion;
import net.pixlies.nations.nations.ranks.NationRank;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Entity("nations")
public class Nation {

    private static final Nations instance = Nations.getInstance();
    private static final NationManager nationManager = new NationManager();

    // INFO
    @Id
    private @Getter @Setter String nationsId;
    private @Getter @Setter String name;
    private @Getter @Setter String description;
    private String leaderUUID;
    private @Getter @Setter long created;

    // DATA
    private @Getter @Setter double politicalPower;
    private @Getter @Setter double money;

    // CUSTOMIZATION
    private String govType;
    private String ideology;
    private String religion;
    private @Getter @Setter List<Integer> constitutionValues;

    // RANKS
    private @Getter @Setter Map<String, NationRank> ranks;

    // MEMBERS
    private @Getter @Setter List<String> memberUUIDs;

    // -------------------------------------------------------------------------------------------------

    public Nation(String nationsId,
                  String name,
                  String description,
                  UUID leaderUUID,
                  long created,
                  double politicalPower,
                  double money,
                  GovernmentType govType,
                  Ideology ideology,
                  Religion religion,
                  List<Integer> constitutionValues,
                  Map<String, NationRank> ranks,
                  List<UUID> memberUUIDs
    ) {
        this.nationsId = nationsId;
        this.name = name;
        this.description = description;
        this.leaderUUID = leaderUUID.toString();
        this.created = created;
        this.politicalPower = politicalPower;
        this.money = money;
        this.govType = govType.name();
        this.ideology = ideology.name();
        this.religion = religion.name();
        this.constitutionValues = constitutionValues;
        this.ranks = ranks;
        List<String> membersStrings = new ArrayList<>();
        memberUUIDs.forEach(uuid -> membersStrings.add(uuid.toString()));
        this.memberUUIDs = membersStrings;
    }

    // -------------------------------------------------------------------------------------------------

    // MANUAL GETTER AND SETTERS
    public void setLeaderUUID(UUID uuid) {
        this.leaderUUID = uuid.toString();
    }

    public UUID getLeaderUUID() {
        return UUID.fromString(this.leaderUUID);
    }

    public void setGovType(GovernmentType type) {
        this.govType = type.name();
    }

    public GovernmentType getGovType() {
        return GovernmentType.valueOf(this.govType);
    }

    public void setIdeology(Ideology type) {
        this.ideology = type.name();
    }

    public Ideology getIdeology() {
        return Ideology.valueOf(this.ideology);
    }

    public void setReligion(Religion type) {
        this.religion = type.name();
    }

    public Religion getReligion() {
        return Religion.valueOf(this.religion);
    }

    public Nation create() {
        ranks.put("leader", NationRank.leader());
        ranks.put("admin", NationRank.admin());
        ranks.put("member", NationRank.member());
        ranks.put("newbie", NationRank.newbie());

        save();
        return this;
    }

    public void save() {
        instance.getNationManager().getNations().put(nationsId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);

    }

    public static Nation getFromId(String id) {
        return nationManager.getNations().get(id);
    }

    private void editConstitution(byte law, int option) {
        constitutionValues.set(law, option);
    }

    
}
