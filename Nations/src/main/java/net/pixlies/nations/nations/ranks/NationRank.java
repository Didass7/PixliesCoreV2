package net.pixlies.nations.nations.ranks;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class NationRank {

    private String name;
    private String prefix;
    private int priority;
    private List<NationPermission> permissions;

    public NationRank(Document document) {
        this.name = document.getString("name");
        this.prefix = document.getString("prefix");
        this.priority = document.getInteger("priority");
        this.permissions = new ArrayList<>() {{
            for (String perm : document.getList("permissions", String.class)) {
                try {
                    add(NationPermission.valueOf(perm));
                } catch (IllegalArgumentException ignored) {
                    // old permissions
                }
            }
        }};
    }

    public Document toDocument() {
        Document document = new Document();

        document.put("name", name);
        document.put("prefix", prefix);
        document.put("priority", priority);
        document.put("permissions", permissions);

        return document;
    }

    // STATICS

    // Newbie has no permissions
    public static NationRank getNewbieRank() {
        List<NationPermission> perms = new ArrayList<>();
        return new NationRank("newbie", "§a*", 1, perms);
    }

    // Member has a few basic permissions
    public static NationRank getMemberRank() {
        List<NationPermission> perms = new ArrayList<>();
        perms.add(NationPermission.INVITE);
        perms.add(NationPermission.BUILD);
        perms.add(NationPermission.INTERACT);
        perms.add(NationPermission.BANK_DEPOSIT);
        return new NationRank("member", "§b**", 32, perms);
    }

    // Admin has quite a few perms
    public static NationRank getAdminRank() {
        List<NationPermission> perms = new ArrayList<>(getMemberRank().permissions);
        perms.add(NationPermission.INVITE);
        perms.add(NationPermission.KICK);
        perms.add(NationPermission.MANAGE_SETTLEMENTS);
        perms.add(NationPermission.CLAIM);
        perms.add(NationPermission.UNCLAIM);
        perms.add(NationPermission.EDIT_RANKS);
        perms.add(NationPermission.BANK_WITHDRAW);
        perms.add(NationPermission.PURCHASE_UPGRADES);
        perms.add(NationPermission.FOREIGN_PERMS);
        return new NationRank("admin", "§c***", 64, perms);
    }

    // Leader has all them perms
    public static NationRank getLeaderRank() {
        List<NationPermission> perms = new ArrayList<>(Arrays.asList(NationPermission.values()));
        return new NationRank("leader", "§6★", 127, perms);
    }

}
