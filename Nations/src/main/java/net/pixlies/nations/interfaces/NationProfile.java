package net.pixlies.nations.interfaces;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.entity.user.User;
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
@Entity
public class NationProfile {

    // -------------------------------------------------------------------------------------------------
    //                                              DATA
    // -------------------------------------------------------------------------------------------------

    // Player
    private String uuid;

    // Nations
    private String nationId;
    private String nationRank;
    private String profileChatType;

    // -------------------------------------------------------------------------------------------------
    //                                            METHODS
    // -------------------------------------------------------------------------------------------------

    public boolean isLeader() {
        Nation nation = Nation.getFromId(nationId);
        NationRank rank = nation.getRanks().get(nationRank);
        return rank.getPriority() == 666;
    }

    public NationRank getRank() {
        return Nation.getFromId(nationId).getRanks().get(nationRank);
    }

    /**
     * Get the nation chat type
     * @return nation chat type
     */
    public ChatType getChatType() {
        return ChatType.valueOf(profileChatType);
    }

    public void setChatType(ChatType chatType) {
        this.profileChatType = chatType.name();
    }

    public Nation getNation() {
        return Nation.getFromId(nationId);
    }

    /**
     * Removes the nation information from a user.
     *
     * @return true if success, false if failed.
     */
    public boolean leaveNation() {
        User user = User.get(getUniqueId());

        if (!isInNation(user)) return false;

        NationProfile profile = get(user);
        if (profile == null) return false;
        Nation nation = Nation.getFromId(profile.getNationId());

        nation.getMemberUUIDs().remove(user.getUuid());
        nation.save();

        user.getExtras().remove("nationsProfile");
        user.save();
        return true;
    }

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    // -------------------------------------------------------------------------------------------------
    //                                          STATIC METHODS

    // -------------------------------------------------------------------------------------------------

    /**
     * Check if player is in a nation by checking their extras variable for a "nationsProfile" object.
     *
     * @param user Expects a valid "User" object of the player
     * @see User
     */
    public static boolean isInNation(@NotNull User user) {
        return user.getExtras().containsKey("nationsProfile");
    }

    /**
     * Get a users NationProfile
     *
     * @param user Expects a valid "User" object of the player
     * @return If user is not in a nation: null; If he is: A valid NationProfile object
     */
    public static @Nullable NationProfile get(@NotNull User user) {
        if (!isInNation(user)) return null;
        return (NationProfile) user.getExtras().get("nationsProfile");
    }

}
