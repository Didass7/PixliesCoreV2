package net.pixlies.nations.nations;

import com.mongodb.client.model.Filters;
import lombok.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.EventUtils;
import net.pixlies.core.utils.RankUtils;
import net.pixlies.nations.Nations;
import net.pixlies.nations.events.impl.NationDisbandEvent;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.chunk.NationChunk;
import net.pixlies.nations.nations.customization.GovernmentType;
import net.pixlies.nations.nations.customization.Ideology;
import net.pixlies.nations.nations.customization.NationConstitution;
import net.pixlies.nations.nations.customization.Religion;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.ranks.NationPermission;
import net.pixlies.nations.nations.ranks.NationRank;
import net.pixlies.nations.nations.relations.Relation;
import net.pixlies.nations.utils.NationUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Nation Class ready to be put in MongoDB because of @Entity
 *
 * @author MickMMars
 * @author Dynmie
 * @author vyketype
 */
@ToString
@EqualsAndHashCode
public class Nation {

    private static final Nations instance = Nations.getInstance();

    // -------------------------------------------------------------------------------------------------
    //                                              DATA
    // -------------------------------------------------------------------------------------------------

    // INFO
    private final @Getter String nationId;
    private @Getter String name;
    private @Getter @Setter String description = NationUtils.randomDescription();
    private @Setter String motd = "";
    private @Getter @Setter UUID leaderUUID;
    private @Getter @Setter long created = System.currentTimeMillis();
    private final @Getter boolean systemNation;
    private @Getter boolean loaded = false;

    // DATA
    private @Getter @Setter double politicalPower = 0.0d;
    private @Getter @Setter double money = 0.0d;

    /**
     * Tax rate for buy orders.
     * In decimal (ex: 0.05 will be a tax of 5%).
     */
    private @Getter @Setter double taxRate = 0.0d;

    // CUSTOMIZATION
    private GovernmentType governmentType = GovernmentType.UNITARY;
    private @Getter @Setter Ideology ideology = Ideology.TRIBAL;
    private @Getter @Setter Religion religion = Religion.SECULAR;
    private @Getter List<Integer> constitutionValues = new ArrayList<>() {{
        for (NationConstitution nc : NationConstitution.values()) {
            add(nc.getDefaultValue());
        }
    }};

    // RANKS
    private @Getter Map<String, NationRank> ranks = new HashMap<>() {{
        NationRank leader = NationRank.getLeaderRank();
        NationRank admin = NationRank.getAdminRank();
        NationRank member = NationRank.getMemberRank();
        NationRank newbie = NationRank.getNewbieRank();

        put(leader.getName(), leader);
        put(admin.getName(), admin);
        put(member.getName(), member);
        put(newbie.getName(), newbie);
    }};

    // MEMBERS
    private @Getter List<UUID> members = new ArrayList<>();

    // CHUNKS
    private @Getter List<NationChunk> claims = new ArrayList<>();

    // RELATIONS
    private @Getter List<String> alliedNationIds = new ArrayList<>();

    // -------------------------------------------------------------------------------------------------
    //                                         CONSTRUCTOR
    // -------------------------------------------------------------------------------------------------

    public Nation(String nationId,
                  String name,
                  String description,
                  String motd,
                  UUID leaderUUID,
                  long created,
                  boolean systemNation,
                  double politicalPower,
                  double money,
                  double taxRate,
                  GovernmentType governmentType,
                  Ideology ideology,
                  Religion religion,
                  List<Integer> constitutionValues,
                  Map<String, NationRank> ranks,
                  List<UUID> members,
                  List<NationChunk> claims,
                  List<String> alliedNationIds) {
        this.nationId = nationId;
        this.name = name;
        this.description = description;
        this.motd = motd;
        this.leaderUUID = leaderUUID;
        this.created = created;
        this.systemNation = systemNation;
        this.politicalPower = politicalPower;
        this.money = money;
        this.taxRate = taxRate;
        this.governmentType = governmentType;
        this.ideology = ideology;
        this.religion = religion;
        this.constitutionValues = constitutionValues;
        this.ranks = ranks;
        this.members = members;
        this.claims = claims;
        this.alliedNationIds = alliedNationIds;
    }

    /**
     * Creates a nation with default settings.
     *
     * @param nationId     The nation ID
     * @param name         the name of the nation
     * @param leaderUUID   the leader's UUID
     * @param systemNation If the nation is owned by the server
     */
    public Nation(@NotNull String nationId, @NotNull String name, @NotNull UUID leaderUUID, boolean systemNation) {
        this.nationId = nationId;
        this.name = name;
        this.leaderUUID = leaderUUID;
        this.systemNation = systemNation;
    }

    // -------------------------------------------------------------------------------------------------
    //                                       GETTERS & SETTERS
    // -------------------------------------------------------------------------------------------------

    // MANUAL GETTER AND SETTERS

    public void changeDescription(@Nullable CommandSender sender, @NotNull String description) {
        this.description = description;

        if (sender == null) {
            return;
        }

        if (sender instanceof Player player) {
            if (!members.contains(player.getUniqueId())) {
                NationsLang.NATION_YOU_CHANGED_DESCRIPTION_BYPASS.send(player, "%NATION%;" + name, "%DESCRIPTION%;" + description);
            }
        } else {
            NationsLang.NATION_YOU_CHANGED_DESCRIPTION_BYPASS.send(sender, "%NATION%;" + name, "%DESCRIPTION%;" + description);
        }

        broadcastNationMembers(NationsLang.NATION_PLAYER_CHANGED_DESCRIPTION.get("%PLAYER%;" + RankUtils.getRankFromSender(sender).getColor() + sender.getName(), "%DESCRIPTION%;" + description));
    }

    public void changeDescription(@NotNull String description) {
        this.changeDescription(null, description);

    }

    public boolean hasMotd() {
        if (motd == null) {
            return false;
        }
        return !motd.equals("");
    }

    public String getMotd() {
        if (hasMotd()) {
            return motd;
        }
        return null;
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

        if (sender instanceof Player player) {
            leaderUUID = player.getUniqueId();
        }

        return this;
    }

    /**
     * Silently create a nation.
     * Async.
     * @return the nation
     */
    public Nation create() {
        return this.create(null);
    }

    private void editConstitution(byte law, int option) {
        if (systemNation) return;
        constitutionValues.set(law, option);
    }

    public void addMember(Player player, String rankName, boolean saveProfile) {
        if (systemNation) return;

        NationProfile profile = NationProfile.get(player.getUniqueId());
        if (!profile.isLoaded()) return;
        if (profile.isInNation()) return;

        // ADD TO MEMBER LIST
        members.add(player.getUniqueId());

        // MAKE A VARIABLE TO STORE THE RANK NAME
        String rankToAddIn = rankName;

        // CHECK IF RANK EXISTS; IF NOT TAKE NEWBIE LOL
        if (!ranks.containsKey(rankToAddIn))
            rankToAddIn = NationRank.getNewbieRank().getName();

        // ASSIGN TO PLAYER AND STORE IT
        profile.setNation(this);
        profile.setNationRank(rankToAddIn);

        if (saveProfile) {
            profile.save();
        }

    }

    /**
     * Disband the nation
     *
     * @param disbander the disbander
     */
    public void disband(@Nullable CommandSender disbander) {
        if (systemNation) return;

        NationDisbandEvent event = new NationDisbandEvent(this);
        EventUtils.call(event);
        if (event.isCancelled()) return;

        this.getMembers().parallelStream().forEach(member -> instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            NationProfile profile = NationProfile.getLoadDoNotCache(member);
            if (!profile.isLoaded()) return;

            profile.leaveNation(false);
            profile.save();
        }));

        this.getAlliedNations().parallelStream().forEach(ally -> {
            this.removeAlly(ally);
            ally.save();
        });

        claims.parallelStream().forEach(NationChunk::unloadClaim);

        this.delete();

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
            NationsLang.NATION_RENAME.broadcast("%NATION%;" + name, "%NEW%;" + newName, "%PLAYER%;" + RankUtils.getRankFromSender(sender).getColor() + sender.getName());
        }

        instance.getNationManager().getNationNames().remove(name);
        instance.getNationManager().getNationNames().put(newName, nationId);
        this.name = newName;
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

    public void delete() {
        this.removeCache();
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () ->
                instance.getMongoManager().getNationsCollection().deleteOne(Filters.eq("nationId", nationId)));
    }

    public void loadClaims() {
        for (NationChunk claim : claims) {
            claim.loadClaim();
        }
    }

    public Collection<Nation> getAlliedNations() {
        List<Nation> nations = new ArrayList<>();
        for (String alliedNationId : alliedNationIds) {
            Nation nation = Nation.getFromId(alliedNationId);
            if (nation == null) continue;
            nations.add(nation);
        }
        return nations;
    }

    public void addAlly(@NotNull Nation toAlly) {
        // OUR NATION
        this.alliedNationIds.add(toAlly.nationId);

        // THEIR NATION
        toAlly.alliedNationIds.add(this.nationId);
    }

    public void removeAlly(@NotNull Nation wasAlly) {
        // OUR NATION
        this.alliedNationIds.remove(wasAlly.nationId);

        // THEIR NATION
        wasAlly.alliedNationIds.remove(this.nationId);
    }

    public Relation getRelationTo(@NotNull Nation toMatch) {
        if (nationId.equals(toMatch.nationId)) {
            return Relation.SAME;
        }

        if (alliedNationIds.contains(toMatch.nationId)) {
            return Relation.ALLY;
        }

        return Relation.OTHER;
    }

    public Document toDocument() {
        Document document = new Document();

        document.put("nationId", nationId);
        document.put("name", name);
        document.put("description", description);
        document.put("motd", motd);
        document.put("leaderUUID", leaderUUID.toString());
        document.put("created", created);
        document.put("systemNation", systemNation);

        document.put("politicalPower", politicalPower);
        document.put("money", money);

        document.put("taxRate", taxRate);

        document.put("governmentType", governmentType.name());

        document.put("ideology", ideology.name());

        document.put("religion", religion.name());

        document.put("constitutionValues", constitutionValues);

        document.put("ranks", ranks.values().stream().map(NationRank::toDocument).toList());

        document.put("members", members.stream().map(UUID::toString).toList());

        document.put("claims", claims.stream().map(NationChunk::toDocument).toList());

        document.put("alliedNationIds", alliedNationIds);

        return document;
    }

    public void loadFromDocument(Document document) {
        // nationId OK
        // name OK
        description = document.get("description", description);
        motd = document.get("motd", motd == null ? "" : motd);
        // leaderUUID OK
        created = document.get("created", created);
        // systemNation OK

        politicalPower = document.get("politicalPower", politicalPower);
        money = document.get("money", money);

        taxRate = document.get("taxRate", taxRate);

        try {
            governmentType = GovernmentType.valueOf(document.get("governmentType", governmentType.name()));
        } catch (IllegalArgumentException ignored) {}

        try {
            ideology = Ideology.valueOf(document.get("ideology", ideology.name()));
        } catch (IllegalArgumentException ignored) {}

        try {
            religion = Religion.valueOf(document.get("religion", religion.name()));
        } catch (IllegalArgumentException ignored) {}

        ranks = new HashMap<>() {{
            try {
                document.get("ranks", new ArrayList<Document>()).stream()
                        .map(NationRank::new)
                        .toList().forEach(r -> put(r.getName(), r));
            } catch (ClassCastException e) {
                putAll(ranks);
            }
        }};

        try {
            members = document.get("members", new ArrayList<String>()).stream()
                    .map(UUID::fromString).collect(Collectors.toList());
        } catch (ClassCastException ignored) {}

        try {
            claims = document.get("claims", new ArrayList<Document>()).stream()
                    .map(NationChunk::new).collect(Collectors.toList());
        } catch (ClassCastException ignored) {}

        try {
            alliedNationIds = document.get("alliedNationIds", new ArrayList<>());
        } catch (ClassCastException ignored) {}

        loaded = true;
    }


    public void save() {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, this::backup);
    }

    /**
     * Not async
     */
    public void backup() {
        if (instance.getMongoManager().getNationsCollection().find(Filters.eq("nationId", nationId)).first() == null) {
            instance.getMongoManager().getNationsCollection().insertOne(this.toDocument());
        }
        instance.getMongoManager().getNationsCollection().replaceOne(Filters.eq("nationId", nationId), this.toDocument());
    }

    public void cache() {
        instance.getNationManager().getNations().put(nationId, this);
        instance.getNationManager().getNationNames().put(name, nationId);
    }

    public void removeCache() {
        instance.getNationManager().getNations().remove(nationId);
        instance.getNationManager().getNationNames().remove(name);
    }


    // -------------------------------------------------------------------------------------------------
    //                                        STATIC METHODS
    // -------------------------------------------------------------------------------------------------

    public static @Nullable Nation getFromId(String id) {
        if (id == null) return null;
        return instance.getNationManager().getNations().get(id);
    }

    public static @Nullable Nation getFromName(String name) {
        if (name == null) return null;
        for (Map.Entry<String, String> entry : instance.getNationManager().getNationNames().entrySet()) {
            String nationName = entry.getKey();
            String nationId = entry.getValue();
            if (nationName.equalsIgnoreCase(name)) {
                return getFromId(nationId);
            }
        }
        return null;
    }

}


