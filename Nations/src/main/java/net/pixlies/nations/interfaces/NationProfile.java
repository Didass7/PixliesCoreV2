package net.pixlies.nations.interfaces;

import dev.morphia.annotations.*;
import dev.morphia.query.experimental.filters.Filters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.CC;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.profile.ChatType;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationRank;
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
@AllArgsConstructor
@Entity("nationProfiles")
@Indexes(
        @Index(fields = { @Field("uuid") })
)
public class NationProfile {

    private static final Nations instance = Nations.getInstance();

    // -------------------------------------------------------------------------------------------------
    //                                              DATA
    // -------------------------------------------------------------------------------------------------

    // Player
    private @Id String uuid;
    private long lastLogin;

    // Nations
    private @Nullable String nationId;
    private @Nullable String nationRank;
    private @Getter(AccessLevel.NONE) String profileChatType;

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
            return ChatType.valueOf(profileChatType);
        } catch (IllegalArgumentException e) {
            return ChatType.GLOBAL;
        }
    }

    public void setChatType(ChatType chatType) {
        this.profileChatType = chatType.name();
        save();
    }

    public void setNation(Nation nation) {
        nationId = nation.getNationsId();
    }

    public Nation getNation() {
        return Nation.getFromId(nationId);
    }

    /**
     * Check if the profile is in a nation.
     *
     * @return True if the profile is indeed in a nation.
     */
    public boolean isInNation() {
        return getNation() != null;
    }

    public void save() {
        instance.getMongoManager().getProfileCache().put(getUniqueId(), this);
        backup();
    }

    public void backup() {
        instance.getMongoManager().getDatastore().save(this);
    }

    /**
     * Removes the nation information from a user.
     */
    public void leaveNation() {

        if (!isInNation()) return;
        Nation nation = getNation();
        if (nation == null) return;

        nationId = null;
        nationRank = null;
        profileChatType = ChatType.GLOBAL.name();
        save();

        nation.getMemberUUIDs().remove(uuid);
        nation.save();
    }

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    // -------------------------------------------------------------------------------------------------
    //                                          STATIC METHODS
    // -------------------------------------------------------------------------------------------------

    public static @NotNull NationProfile get(UUID uuid) {
        if (!instance.getMongoManager().getProfileCache().containsKey(uuid)) return getFromDatabase(uuid);
        return instance.getMongoManager().getProfileCache().get(uuid);
    }

    private static NationProfile getFromDatabase(UUID uuid) {

        NationProfile profile = instance.getMongoManager().getDatastore().find(NationProfile.class).filter(Filters.gte("uuid", uuid.toString())).first();
        if (profile == null) {
            profile = new NationProfile(
                    uuid.toString(),
                    System.currentTimeMillis(),
                    null,
                    null,
                    ChatType.GLOBAL.toString()
            );

            profile.save();
            instance.getLogger().info(CC.format("&bNationProfile for &6" + uuid + "&b created in database."));
            return profile;
        }

        // NULL CHECK
        NationProfile checkProfile = new NationProfile(
                profile.uuid,
                profile.lastLogin,
                profile.nationId,
                profile.nationRank,
                profile.profileChatType == null ? ChatType.GLOBAL.name() : profile.profileChatType
        );

        checkProfile.save();
        return checkProfile;
    }

}
