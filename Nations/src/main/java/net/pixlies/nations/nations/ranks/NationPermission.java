package net.pixlies.nations.nations.ranks;

import net.pixlies.core.entity.User;
import net.pixlies.nations.interfaces.NationProfile;
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
    MANAGE_TAX
    ;

    public boolean hasPermission(CommandSender sender) {
        if (!(sender instanceof Player)) return true;
        User user = User.get(((Player) sender).getUniqueId());
        if (user.getSettings().hasNationBypass()) return true;
        return hasNationPermission(user);
    }

    public boolean hasNationPermission(User user) {
        NationProfile profile = NationProfile.get(user);
        if (profile == null) return false;
        if (user.getSettings().hasNationBypass()) return true;
        if (profile.isLeader()) return true;
        NationRank rank = profile.getRank();
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
