package net.pixlies.nations.nations.ranks;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Rank {

    private String name;
    private String prefix;
    private int priority;
    private List<String> permissions;

    @SuppressWarnings("unchecked")
    public static Rank get(Map<String, Object> map) {
        return new Rank((String) map.get("name"),
                (String) map.get("prefix"),
                (Short) map.get("priority"),
                (List<String>) map.get("permissions"));
    }

    public Map<String, Object> toMap() {
        Map<String, Object> returner = new HashMap<>();
        returner.put("name", name);
        returner.put("prefix", prefix);
        returner.put("priority", priority);
        returner.put("permissions", permissions);
        return returner;
    }

    // Newbie has no permissions
    public static Rank newbie() {
        List<String> perms = new ArrayList<>();
        return new Rank("newbie", "§a*", 1, perms);
    }

    // Member has a few basic permissions
    public static Rank member() {
        List<String> perms = new ArrayList<>();
        perms.add(Permission.INVITE.name());
        perms.add(Permission.BUILD.name());
        perms.add(Permission.INTERACT.name());
        perms.add(Permission.BANK_DEPOSIT.name());
        return new Rank("member", "§b**", 32, perms);
    }

    // Admin has quite a few perms
    public static Rank admin() {
        List<String> perms = new ArrayList<>(member().permissions);
        perms.add(Permission.INVITE.name());
        perms.add(Permission.KICK.name());
        perms.add(Permission.MANAGE_SETTLEMENTS.name());
        perms.add(Permission.CLAIM.name());
        perms.add(Permission.UNCLAIM.name());
        perms.add(Permission.EDIT_RANKS.name());
        perms.add(Permission.BANK_WITHDRAW.name());
        perms.add(Permission.PURCHASE_UPGRADES.name());
        perms.add(Permission.FOREIGN_PERMS.name());
        return new Rank("admin", "§c***", 64, perms);
    }

    public static Rank leader() {
        List<String> perms = new ArrayList<>();

        return new Rank("leader", "§6★", 127, perms);
    }

}
