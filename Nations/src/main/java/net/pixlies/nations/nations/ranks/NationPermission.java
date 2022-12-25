package net.pixlies.nations.nations.ranks;

import net.pixlies.core.entity.user.User;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum NationPermission {

    INVITE,
    KICK,

    CLAIM,
    UNCLAIM,

    BUILD,
    INTERACT,

    MANAGE_SETTLEMENTS,
    EDIT_RANKS,
    MANAGE_RELATIONS,

    BANK_DEPOSIT,
    BANK_WITHDRAW,
    PURCHASE_UPGRADES,

    FOREIGN_PERMS,

    CHANGE_DESCRIPTION,
    CHANGE_NAME,
    CHANGE_LEADERSHIP,

    EDIT_FLAGS,

    DECLARE_WAR,
    JUSTIFY_WAR_GOAL,
    MANAGE_WAR,

    SET_WIKI_LINK,

    CHANGE_CAPITAL,

    CHUNK_MENU,

    MANAGE_ELECTIONS,
    MANAGE_TAX,
    MANAGE_TARIFFS;

    public boolean hasPermission(CommandSender sender) {
        if (!(sender instanceof Player player)) return true;
        User user = User.get(player.getUniqueId());
        if (user.isBypassing()) return true;
        return hasPermission(NationProfile.get(player.getUniqueId()));
    }

    public boolean hasPermission(NationProfile profile) {
        if (!profile.isInNation()) return false;
        if (profile.isNationLeader()) return true;
        NationRank rank = profile.getRank();
        if (rank == null) return false;
        return rank.getPermissions().contains(this.name());
    }

    public static boolean exists(String name) {
        for (NationPermission p : values()) {
            if (p.name().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

}
