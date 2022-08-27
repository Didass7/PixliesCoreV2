package net.pixlies.nations.interfaces;

import com.mongodb.client.model.Filters;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.profile.ChatType;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationRank;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A Morphia-serializable Object to store all important information about the players
 * Nation information.
 *
 * @author MickMMars
 * @author Dynmie
 */
@Data
public class NationProfile {

    private static final Nations instance = Nations.getInstance();

    // -------------------------------------------------------------------------------------------------
    //                                              DATA
    // -------------------------------------------------------------------------------------------------

    // Player
    private boolean loaded = false;
    private final String uuid;
    private long lastLogin = System.currentTimeMillis();
    private boolean hasJoinedBefore = false;

    // Nations
    private @Nullable String nationId;
    private @Nullable String nationRank;
    private @Getter(AccessLevel.NONE) String chatType = ChatType.GLOBAL.name();
    private @Getter(AccessLevel.NONE) boolean autoClaim = false;

    // -------------------------------------------------------------------------------------------------
    //                                          CONSTRUCTOR
    // -------------------------------------------------------------------------------------------------

    public NationProfile(UUID uniqueId) {
        this.uuid = uniqueId.toString();
    }

    // -------------------------------------------------------------------------------------------------
    //                                            METHODS
    // -------------------------------------------------------------------------------------------------

    public boolean isNationLeader() {
        Nation nation = getNation();
        if (nation == null) return false;
        return nation.getLeaderUUID().toString().equals(uuid);
    }

    public @Nullable NationRank getRank() {
        Nation nation = getNation();
        if (nation == null) return null;

        return getNation().getRanks().getOrDefault(nationRank, NationRank.getNewbieRank());
    }

    /**
     * Get the nation chat type
     *
     * @return nation chat type
     */
    public ChatType getChatType() {
        try {
            return ChatType.valueOf(chatType);
        } catch (IllegalArgumentException e) {
            return ChatType.GLOBAL;
        }
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType.name();
    }

    public void setNation(Nation nation) {
        nationId = nation.getNationId();
    }

    public Nation getNation() {
        return Nation.getFromId(nationId);
    }

    public boolean isAutoClaiming() {
        if (!isInNation()) {
            if (autoClaim) {
                autoClaim = false;
            }
            return false;
        }
        return autoClaim;
    }

    public void setAutoClaiming(boolean value) {
        if (!isInNation()) return;
        autoClaim = value;
    }

    /**
     * Check if the profile is in a nation.
     *
     * @return True if the profile is indeed in a nation.
     */
    public boolean isInNation() {
        return getNation() != null;
    }

    /**
     * Removes the nation information from a user.
     */
    public void leaveNation(boolean saveNation) {

        if (!isInNation()) return;
        Nation nation = getNation();
        if (nation == null) return;

        nationId = null;
        nationRank = null;
        chatType = ChatType.GLOBAL.name();

        nation.getMemberUUIDs().remove(uuid);

        if (saveNation) {
            nation.save();
        }

    }

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    /**
     * Async backup
     */
    public void save() {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, this::backup);
    }

    /**
     * Non sync backup
     */
    public void backup() {
        if (instance.getMongoManager().getNationProfileCollection().find(Filters.eq("uuid", uuid)).first() == null) {
            instance.getMongoManager().getNationProfileCollection().insertOne(toDocument());
        }
        instance.getMongoManager().getNationProfileCollection().replaceOne(Filters.eq("uuid", uuid), toDocument());
    }

    public Document toDocument() {
        Document document = new Document();

        document.put("uuid", uuid);
        document.put("lastLogin", lastLogin);

        document.put("nationId", nationId);
        document.put("nationRank", nationRank);
        document.put("chatType", chatType);
        document.put("autoClaim", autoClaim);

        return document;
    }

    public void loadFromDocument(Document document) {
        lastLogin = document.getLong("lastLogin") == null ? lastLogin : document.getLong("lastLogin");

        nationId = document.getString("nationId") == null ? nationId : document.getString("nationId");
        nationRank = document.getString("nationRank") == null ? nationRank : document.getString("nationRank");
        chatType = document.getString("chatType") == null ? chatType : document.getString("chatType");
        autoClaim = document.getBoolean("autoClaim") == null ? autoClaim : document.getBoolean("autoClaim");
    }

    public void load(boolean cache) {
        Document document = instance.getMongoManager().getNationProfileCollection().find(Filters.eq("uuid", uuid)).first();
        if (document == null) {
            backup();
            loaded = true;
            if (cache) {
                instance.getMongoManager().getProfileCache().put(getUniqueId(), this);
            }
            return;
        }
        loadFromDocument(document);
        hasJoinedBefore = true;
        loaded = true;

        if (cache) {
            instance.getMongoManager().getProfileCache().put(getUniqueId(), this);
        }
    }

    // -------------------------------------------------------------------------------------------------
    //                                          STATIC METHODS
    // -------------------------------------------------------------------------------------------------

    public static @NotNull NationProfile get(UUID uuid) {
        if (!instance.getMongoManager().getProfileCache().containsKey(uuid)) {
            instance.getMongoManager().getProfileCache().put(uuid, new NationProfile(uuid));
        }
        return instance.getMongoManager().getProfileCache().get(uuid);
    }

    public static NationProfile getLoadDoNotCache(UUID uuid) {
        if (instance.getMongoManager().getProfileCache().containsKey(uuid)) {
            NationProfile profile = instance.getMongoManager().getProfileCache().get(uuid);
            if (!profile.isLoaded()) {
                profile.load(false);
            }
        }
        NationProfile profile = new NationProfile(uuid);
        profile.load(false);
        return profile;
    }

    public static void loadAllOnlineUsers() {
        for (Player player : instance.getServer().getOnlinePlayers()) {
            NationProfile profile = NationProfile.get(player.getUniqueId());
            if (!profile.isLoaded() && player.isOnline()) {
                profile.load(true);
            }
        }
    }

}
