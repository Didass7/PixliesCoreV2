package net.pixlies.nations.nations;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.entity.User;
import net.pixlies.core.events.PixliesCancellableEvent;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.EventUtils;
import net.pixlies.nations.Nations;
import net.pixlies.nations.events.impl.NationDisbandEvent;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.customization.GovernmentType;
import net.pixlies.nations.nations.customization.Ideology;
import net.pixlies.nations.nations.customization.Religion;
import net.pixlies.nations.nations.ranks.NationRank;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Nation Class ready to be put in MongoDB because of @Entity
 *
 * @author MickMMars
 * @author Dynmie
 * @author vPrototype_
 */
@AllArgsConstructor
@Entity("nations")
public class Nation {

    // -------------------------------------------------------------------------------------------------
    //                                              DATA
    // -------------------------------------------------------------------------------------------------

    private static final Nations instance = Nations.getInstance();

    // INFO
    @Id private @Getter @Setter String nationsId;
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

    // CHUNKS
    private @Getter @Setter List<NationChunk> claims;

    // -------------------------------------------------------------------------------------------------
    //                                         CONSTRUCTOR
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
                  List<UUID> memberUUIDs,
                  List<NationChunk> claims
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
        this.claims = claims;
    }

    // -------------------------------------------------------------------------------------------------
    //                                       GETTERS & SETTERS
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

    public List<UUID> getMembers() {
        List<UUID> returner = new ArrayList<>();
        for (String s : memberUUIDs)
            returner.add(UUID.fromString(s));
        return returner;
    }

    // -------------------------------------------------------------------------------------------------
    //                                            METHODS
    // -------------------------------------------------------------------------------------------------

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
        instance.getNationManager().getNameNations().put(name, nationsId);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

    private void editConstitution(byte law, int option) {
        constitutionValues.set(law, option);
    }

    public void addMember(User user, String rank) {
        // ADD TO MEMBER LIST
        memberUUIDs.add(user.getUuid());
        save();

        // MAKE A VARIABLE TO STORE THE RANK NAME
        String rankToAddIn = rank;

        // CHECK IF RANK EXISTS; IF NOT TAKE NEWBIE
        if (!ranks.containsKey(rankToAddIn)) rankToAddIn = NationRank.newbie().getName();

        // CREATE A NEW NATIONPROFILE INSTANCE; ASSIGN TO PLAYER AND STORE IT
        NationProfile nProfile = new NationProfile(
                user.getUuid(),
                nationsId,
                rankToAddIn
        );
        user.getExtras().put("nationsProfile", nProfile);
        user.save();
    }

    /**
     * Disband the nation
     * @param disbander the disbander
     */
    public boolean disband(@Nullable CommandSender disbander) {

        PixliesCancellableEvent event = EventUtils.callCancelable(new NationDisbandEvent(this));
        if (event.isCancelled()) return false;

        for (UUID member : getMembers()) {
            User memberUser = User.get(member);
            NationProfile profile = NationProfile.get(memberUser);
            if (profile != null)
                profile.leaveNation();
        }

        instance.getNationManager().getNameNations().remove(name);
        instance.getNationManager().getNations().remove(nationsId);

        instance.getMongoManager().getDatastore().delete(this);

        if (disbander != null) {
            Lang.NATION_DISBANDED.broadcast("%NATION%;" + name, "%PLAYER%;" + disbander.getName());
        }

        return true;

    }

    /**
     * Shortened disband, will not broadcast.
     */
    public void disband() {
        this.disband(null);
    }

    public void rename(@Nullable CommandSender sender, @NotNull String newName) {

        if (newName.length() > 16 || newName.isEmpty() || newName.matches("^[a-zA-Z0-9_-]*$") ) {
            throw new IllegalArgumentException("Illegal nation name: " + newName);
        }

        instance.getNationManager().getNameNations().remove(this.name);
        instance.getNationManager().getNameNations().put(newName, nationsId);

        if (sender != null) {
            Lang.NATION_RENAME.broadcast("%NATION%;" + name, "%NEW%;" + newName, "%PLAYER%;" + sender.getName());
        }

        this.name = newName;
        save();

    }

    /**
     * Shortened rename, will not broadcast.
     * @param newName the new name
     */
    public void rename(@NotNull String newName) {
        this.rename(null, newName);
    }

    // -------------------------------------------------------------------------------------------------
    //                                        STATIC METHODS
    // -------------------------------------------------------------------------------------------------

    public static Nation getFromId(String id) {
        return instance.getNationManager().getNations().get(id);
    }

    public static Nation getFromName(String name) {
        return instance.getNationManager().getNations().get(instance.getNationManager().getNameNations().get(name));
    }

}
