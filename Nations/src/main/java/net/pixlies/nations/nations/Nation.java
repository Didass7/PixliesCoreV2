package net.pixlies.nations.nations;

import dev.morphia.annotations.*;
import lombok.*;
import net.pixlies.core.events.PixliesCancellableEvent;
import net.pixlies.core.utils.EventUtils;
import net.pixlies.nations.Nations;
import net.pixlies.nations.events.impl.NationDisbandEvent;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.customization.GovernmentType;
import net.pixlies.nations.nations.customization.Ideology;
import net.pixlies.nations.nations.customization.NationConstitution;
import net.pixlies.nations.nations.customization.Religion;
import net.pixlies.nations.nations.ranks.NationPermission;
import net.pixlies.nations.nations.ranks.NationRank;
import net.pixlies.nations.utils.NationUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Nation Class ready to be put in MongoDB because of @Entity
 *
 * @author MickMMars
 * @author Dynmie
 * @author vPrototype_
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Entity("nations")
@Indexes(
        @Index(fields = { @Field("nationId") })
)
public class Nation {

    private static final Nations instance = Nations.getInstance();

    // -------------------------------------------------------------------------------------------------
    //                                              DATA
    // -------------------------------------------------------------------------------------------------

    // INFO
    @Id
    private @Getter @Setter String nationId;
    private @Getter @Setter String name;
    private @Getter String description;
    private @Getter @Setter String motd;
    private String leaderUUID;
    private @Getter @Setter long created;
    private final @Getter boolean systemNation;

    // DATA
    private @Getter @Setter double politicalPower;
    private @Getter @Setter double money;

    /**
     * Tax rate for buy orders.
     * In decimal (ex: 0.05 will be a tax of 5%).
     */
    private @Getter @Setter double taxRate;

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

    public Nation(@NotNull String nationId,
                  @NotNull String name,
                  @NotNull String description,
                  @NotNull String motd,
                  @NotNull UUID leaderUUID,
                  long created,
                  boolean systemNation,
                  double politicalPower,
                  double money,
                  double taxRate,
                  @NotNull GovernmentType govType,
                  @NotNull Ideology ideology,
                  @NotNull Religion religion,
                  @NotNull List<Integer> constitutionValues,
                  @NotNull Map<String, NationRank> ranks,
                  @NotNull List<UUID> memberUUIDs,
                  @NotNull List<NationChunk> claims
    ) {
        this.nationId = nationId;
        this.name = name;
        this.description = description;
        this.motd = motd;
        this.leaderUUID = leaderUUID.toString();
        this.created = created;
        this.systemNation = systemNation;
        this.politicalPower = politicalPower;
        this.money = money;
        this.taxRate = taxRate;
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

    /**
     * Creates a nation with default settings.
     *
     * @param nationId   The nation ID
     * @param name       the name of the nation
     * @param leaderUUID the leader's UUID
     */
    public Nation(@NotNull String nationId, @NotNull String name, @NotNull UUID leaderUUID) {
        this(
                nationId,
                name,
                NationUtils.randomDescription(),
                "",
                leaderUUID,
                System.currentTimeMillis(),
                false,
                0.0,
                0.0,
                0.0,
                GovernmentType.UNITARY,
                Ideology.TRIBAL,
                Religion.SECULAR,
                new ArrayList<>() {{
                    for (NationConstitution nc : NationConstitution.values()) {
                        add(nc.getDefaultValue());
                    }
                }},
                new HashMap<>() {{
                    NationRank leader = NationRank.getLeaderRank();
                    NationRank admin = NationRank.getAdminRank();
                    NationRank member = NationRank.getMemberRank();
                    NationRank newbie = NationRank.getNewbieRank();

                    put(leader.getName(), leader);
                    put(admin.getName(), admin);
                    put(member.getName(), member);
                    put(newbie.getName(), newbie);
                }},
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    // -------------------------------------------------------------------------------------------------
    //                                       GETTERS & SETTERS
    // -------------------------------------------------------------------------------------------------

    // MANUAL GETTER AND SETTERS

    public void setLeaderUUID(UUID uuid) {
        if (systemNation) return;
        this.leaderUUID = uuid.toString();
        save();
        backup();
    }

    public UUID getLeaderUUID() {
        return UUID.fromString(this.leaderUUID);
    }

    public void setGovType(GovernmentType type) {
        if (systemNation) return;
        this.govType = type.name();
        save();
        backup();
    }

    public GovernmentType getGovType() {
        return GovernmentType.valueOf(this.govType);
    }

    public void setIdeology(Ideology type) {
        if (systemNation) return;
        this.ideology = type.name();
        save();
        backup();
    }

    public Ideology getIdeology() {
        return Ideology.valueOf(this.ideology);
    }

    public void setReligion(Religion type) {
        if (systemNation) return;
        this.religion = type.name();
        save();
        backup();
    }

    public Religion getReligion() {
        return Religion.valueOf(this.religion);
    }

    public Collection<UUID> getMembers() {
        List<UUID> returner = new ArrayList<>();
        for (String s : memberUUIDs)
            returner.add(UUID.fromString(s));
        return returner;
    }

    public boolean hasMotd() {
        if (motd == null) {
            return false;
        }
        return !motd.equals("");
    }

    public void setDescription(String description) {
        if (systemNation) return;
        this.description = description;
        save();
        backup();
        // TODO: CHANGE DESC BROADCAST
    }

    public Collection<UUID> getOnlineMembers() {
        List<UUID> onlinePlayers = new ArrayList<>();
        for (UUID uuid : getMembers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer.isOnline()) {
                onlinePlayers.add(uuid);
            }
        }
        return onlinePlayers;
    }

    public Collection<Player> getOnlineMembersAsPlayer() {
        List<Player> onlinePlayers = new ArrayList<>();
        for (UUID uuid : getMembers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) onlinePlayers.add(player);
        }
        return onlinePlayers;
    }

    // -------------------------------------------------------------------------------------------------
    //                                            METHODS
    // -------------------------------------------------------------------------------------------------

    public Nation create(@Nullable CommandSender sender) {
        ranks.clear();
        ranks.put("leader", NationRank.getLeaderRank());
        ranks.put("admin", NationRank.getAdminRank());
        ranks.put("member", NationRank.getMemberRank());
        ranks.put("newbie", NationRank.getNewbieRank());

        if (sender != null) {
            NationsLang.NATION_FORMED.broadcast("%NATION%;" + this.getName(), "%PLAYER%;" + sender.getName());
        }

        save();
        return this;
    }

    /**
     * Silently create a nation.
     *
     * @return the nation
     */
    public Nation create() {
        return this.create(null);
    }

    /**
     * Backup will NOT be run! Make sure you back up manually!
     */
    public void save() {
        instance.getNationManager().getNations().put(nationId, this);
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

    private void editConstitution(byte law, int option) {
        if (systemNation) return;
        constitutionValues.set(law, option);
    }

    public void addMember(Player player, String rank) {
        if (systemNation) return;

        NationProfile profile = NationProfile.get(player.getUniqueId());
        if (profile.isInNation()) return;

        // ADD TO MEMBER LIST
        memberUUIDs.add(player.getUniqueId().toString());
        save();

        // MAKE A VARIABLE TO STORE THE RANK NAME
        String rankToAddIn = rank;

        // CHECK IF RANK EXISTS; IF NOT TAKE NEWBIE LOL
        if (!ranks.containsKey(rankToAddIn)) rankToAddIn = NationRank.getNewbieRank().getName();

        // ASSIGN TO PLAYER AND STORE IT
        profile.setNation(this);
        profile.setNationRank(rank);
        profile.save();

    }

    /**
     * Disband the nation
     *
     * @param disbander the disbander
     */
    public void disband(@Nullable CommandSender disbander) {
        if (systemNation) return;

        PixliesCancellableEvent event = EventUtils.callCancelable(new NationDisbandEvent(this));
        if (event.isCancelled()) return;

        for (UUID member : getMembers()) {
            NationProfile profile = NationProfile.get(member);
            profile.leaveNation();
        }

        instance.getNationManager().getNations().remove(nationId);
        instance.getMongoManager().getDatastore().delete(this);

        if (disbander != null) {
            NationsLang.NATION_DISBANDED.broadcast("%NATION%;" + name, "%PLAYER%;" + disbander.getName());
        }

    }

    /**
     * Shortened disband, will not broadcast.
     */
    public void disband() {
        this.disband(null);
    }

    public void rename(@Nullable CommandSender sender, @NotNull String newName) {
        if (systemNation) return;

        if (!NationUtils.nameValid(name)) {
            throw new IllegalArgumentException("Illegal nation name: " + newName);
        }

        if (sender != null) {
            NationsLang.NATION_RENAME.broadcast("%NATION%;" + name, "%NEW%;" + newName, "%PLAYER%;" + sender.getName());
        }

        this.name = newName;
        save();

    }

    /**
     * Shortened rename, will not broadcast.
     *
     * @param newName the new name
     */
    public void rename(@NotNull String newName) {
        this.rename(null, newName);
    }

    /**
     * Broadcasts a message to all Nation members with a permission
     * @param message The message to broadcast
     * @param permission The permission
     */
    public void broadcastNationMembers(String message, @Nullable NationPermission permission) {
        getOnlineMembersAsPlayer().forEach(player -> {
            if (permission != null) {
                if (!permission.hasPermission(NationProfile.get(player.getUniqueId()))) {
                    return;
                }
            }
            player.sendMessage(message);
        });
    }

    public void broadcastNationMembers(String message) {
        this.broadcastNationMembers(message, null);
    }

    // -------------------------------------------------------------------------------------------------
    //                                        STATIC METHODS
    // -------------------------------------------------------------------------------------------------

    public static @Nullable Nation getFromId(String id) {
        if (id == null) return null;
        for (Map.Entry<String, Nation> entry : instance.getNationManager().getNations().entrySet()) {
            String nationId = entry.getKey();
            Nation nation = entry.getValue();
            if (nationId.equals(id)) return nation;
        }
        return null;
    }

    public static @Nullable Nation getFromName(String name) {
        if (name == null) return null;
        for (Nation nation : instance.getNationManager().getNations().values()) {
            if (nation.getName().equalsIgnoreCase(name)) return nation;
        }
        return null;
    }

    public static Nation getNullCheckedNation(Nation nation) {
        return new Nation(
                nation.nationId,
                nation.name,
                nation.description == null ? "" : nation.description,
                nation.motd == null ? "" : nation.motd,
                nation.leaderUUID,
                nation.created,
                nation.systemNation,
                nation.politicalPower,
                nation.money,
                nation.taxRate,
                nation.govType,
                nation.ideology,
                nation.religion,
                nation.constitutionValues,
                nation.ranks == null ? new HashMap<>() : nation.ranks,
                nation.memberUUIDs == null ? new ArrayList<>() : nation.memberUUIDs,
                nation.claims == null ? new ArrayList<>() : nation.claims
        );
    }

    public static @NotNull Nation createSystemNation(String id) {
        return new Nation(
                id,
                id,
                NationUtils.randomDescription(),
                "",
                UUID.randomUUID(),
                System.currentTimeMillis(),
                true,
                0.0,
                0.0,
                0.0,
                GovernmentType.UNITARY,
                Ideology.TRIBAL,
                Religion.SECULAR,
                new ArrayList<>(),
                new HashMap<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

}


