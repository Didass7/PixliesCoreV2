package net.pixlies.nations.nations.ranks;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
@Entity
public class NationRank {

    private String name;
    private String prefix;
    private int priority;
    private List<String> permissions;

/*    @SuppressWarnings("unchecked")
    public static NationRank get(Map<String, Object> map) {
        return new NationRank((String) map.get("name"),
                (String) map.get("prefix"),
                (Short) map.get("priority"),
                (List<String>) map.get("permissions"));
    }*/

/*    public Map<String, Object> toMap() {
        Map<String, Object> returner = new HashMap<>();
        returner.put("name", name);
        returner.put("prefix", prefix);
        returner.put("priority", priority);
        returner.put("permissions", permissions);
        return returner;
    }*/

    // Newbie has no permissions
    public static NationRank newbie() {
        List<String> perms = new ArrayList<>();
        return new NationRank("newbie", "§a*", 1, perms);
    }

    // Member has a few basic permissions
    public static NationRank member() {
        List<String> perms = new ArrayList<>();
        perms.add(NationPermission.INVITE.name());
        perms.add(NationPermission.BUILD.name());
        perms.add(NationPermission.INTERACT.name());
        perms.add(NationPermission.BANK_DEPOSIT.name());
        return new NationRank("member", "§b**", 32, perms);
    }

    // Admin has quite a few perms
    public static NationRank admin() {
        List<String> perms = new ArrayList<>(member().permissions);
        perms.add(NationPermission.INVITE.name());
        perms.add(NationPermission.KICK.name());
        perms.add(NationPermission.MANAGE_SETTLEMENTS.name());
        perms.add(NationPermission.CLAIM.name());
        perms.add(NationPermission.UNCLAIM.name());
        perms.add(NationPermission.EDIT_RANKS.name());
        perms.add(NationPermission.BANK_WITHDRAW.name());
        perms.add(NationPermission.PURCHASE_UPGRADES.name());
        perms.add(NationPermission.FOREIGN_PERMS.name());
        return new NationRank("admin", "§c***", 64, perms);
    }

    // Leader has all them perms
    public static NationRank leader() {
        List<String> perms = new ArrayList<>();
        Arrays.stream(NationPermission.values()).forEach(perm -> perms.add(perm.name()));
        return new NationRank("leader", "§6★", 127, perms);
    }

}
