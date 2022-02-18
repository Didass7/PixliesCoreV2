package net.pixlies.nations.interfaces;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.entity.User;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.ranks.NationRank;

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
    private UUID uniqueId;

    // Nations
    private String nationId;
    private String nationRank;

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
     * Removes the nation information from a user.
     *
     * @return true if success, false if failed.
     */
    public boolean leaveNation() {
        User user = User.get(uniqueId);

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

    // -------------------------------------------------------------------------------------------------
    //                                          STATIC METHODS

    // -------------------------------------------------------------------------------------------------

    /**
     * Check if player is in a nation by checking their extras variable for a "nationsProfile" object.
     *
     * @param user Expects a valid "User" object of the player
     * @see User
     */
    public static boolean isInNation(User user) {
        return user.getExtras().containsKey("nationsProfile");
    }

    /**
     * Get a users NationProfile
     *
     * @param user Expects a valid "User" object of the player
     * @return If user is not in a nation: null; If he is: A valid NationProfile object
     */
    public static NationProfile get(User user) {
        if (!isInNation(user)) return null;
        return (NationProfile) user.getExtras().get("nationsProfile");
    }

}
