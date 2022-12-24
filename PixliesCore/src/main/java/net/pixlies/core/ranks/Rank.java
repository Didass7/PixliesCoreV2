package net.pixlies.core.ranks;

import lombok.Getter;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.pixlies.core.utils.CC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public enum Rank {

    // LOWEST
    DEFAULT("default", 5000, "Default", "&7", "", "&7", true),

    // DONOR
    TOPAZ("topaz", 7000, "Topaz", "&x&5&f&d&6&e&3&lT&x&5&8&c&5&d&1&lO&x&5&2&b&4&b&f&lP&x&4&d&a&6&b&0&lA&x&4&3&8&c&9&4&lZ ", "", "&b&l", true),
    AMETHYST("amethyst", 7050, "Amethyst", "&x&e&b&2&3&e&4&lA&x&c&9&1&c&c&3&lM&x&b&5&1&9&b&0&lE&x&a&3&1&7&9&f&lT&x&9&4&1&6&9&0&lH&x&8&f&1&1&8&b&lY&x&8&0&1&1&7&c&lS&x&6&b&0&e&6&8&lT ", "", "&d", true),
    SAPPHIRE("sapphire", 7150, "Sapphire", "&x&3&f&5&d&f&2&lS&x&3&8&5&2&d&1&lA&x&3&7&4&f&c&4&lP&x&3&4&4&9&b&3&lP&x&4&1&5&6&9&6&lH&x&2&e&4&0&9&e&lI&x&2&c&3&c&9&1&lR&x&2&6&3&4&8&0&lE ", "", "&9&l", true),
    RUBY("ruby", 7200, "Ruby", "&x&f&f&3&b&3&b&lR&x&d&e&3&5&3&5&lU&x&c&4&2&f&2&f&lB&x&a&6&2&8&2&8&lY ", "", "&c&l", true),

    // SPECIAL
    YOUTUBER("youtuber", 9000, "YouTube", "&c&lY&f&lT ", "", "&f", false),

    // STAFF
    BUILDER("builder", 1300, "Builder", "&b&lBUILDER ", "", "&b&l", false),
    HELPER("helper", 1250, "Helper", "&x&F&0&3&4&D&C&lH&x&E&3&3&3&D&0&lE&x&D&4&3&0&C&2&lL&x&C&4&2&E&B&4&lP&x&B&0&2&A&A&2&lE&x&A&6&2&9&9&9&lR ", "", "&x&F&0&3&4&D&C&l", false),
    MODERATOR("mod", 1200, "Moderator", "&x&F&A&F&F&3&6&lM&x&E&F&F&4&3&8&lO&x&D&F&E&3&3&C&lD ", "", "&x&F&A&F&F&3&6&l", false),
    ADMIN("admin", 1150, "Admin", "&x&F&9&3&5&3&5&l&k|&r&x&F&9&3&5&3&5&lA&x&E&6&3&2&3&2&lD&x&D&2&3&5&3&5&lM&x&C&3&3&2&3&2&lI&x&B&0&2&F&2&F&lN&k|&r&x&B&0&2&F&2&F&l ", "", "&x&F&0&3&4&3&4&l", false),
    DEVELOPER("dev", 1100, "Developer", "&x&2&6&E&0&D&8&l&k|&r&x&2&6&E&0&D&8&lD&x&2&4&C&A&C&3&lE&x&2&2&A&E&A&8&lV&k|&r&x&2&2&A&E&A&8&l ", "", "&x&2&6&E&0&D&8&l", false),
    MANAGER("manager", 1050, "Manager", "&x&3&4&A&B&C&3&l&k|&r&x&3&4&A&B&C&3&lM&x&3&3&A&1&B&8&lA&x&3&0&9&5&A&A&lN&x&3&2&8&A&9&C&lA&x&3&6&8&2&9&2&lG&x&3&7&7&8&8&5&lE&x&3&A&7&5&8&1&lR&k|&r&x&3&A&7&5&8&1&l ", "", "&x&3&4&A&B&C&3&l", false),
    OWNER("owner", 1000, "Owner", "&c&l&k|&c&lOWNER&k|&c&l ", "", "&c&l", false);

    // HIGHEST

    @Getter private final String identifier;
    @Getter private final int tabListPriority;
    @Getter private final String displayName;
    @Getter private final String prefix;
    @Getter private final String suffix;
    @Getter private final String color;
    @Getter private final boolean promotable;

    Rank(String identifier, int tabListPriority, String displayName, String prefix, String suffix, String color, boolean promotable) {
        this.identifier = identifier;
        this.tabListPriority = tabListPriority;
        this.displayName = CC.format(displayName);
        this.prefix = CC.format(prefix);
        this.suffix = CC.format(suffix);
        this.color = CC.format(color);
        this.promotable = promotable;
    }

    public int getPriority() {
        return ordinal();
    }

    public boolean isAbove(Rank rank) {
        return this.getPriority() > rank.getPriority();
    }

    public boolean hasRank(Rank rank) {
        return rank != null && this.getPriority() >= rank.getPriority();
    }

    public String getChatPrefix() {
        return this.getPrefix() + this.getColor();
    }

    public String getChatSuffix() {
        return this.getSuffix();
    }

    public static Collection<String> getDisplayNames() {
        List<String> list = new ArrayList<>();
        for (Rank rank : Rank.values()) {
            list.add(rank.getDisplayName());
        }
        return list;
    }

    public static Rank getByDisplayName(String displayName) {
        for (Rank rank : Rank.values()) {
            if (rank.displayName.equals(displayName)) return rank;
        }
        return null;
    }

    public static Rank getRankByIdentifier(String identifier) {
        for (Rank rank : Rank.values()) {
            if (rank.identifier.equals(identifier)) return rank;
        }
        return null;
    }

    public static Rank getRank(UUID uuid) {
        User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
        if (user == null) return Rank.DEFAULT;

        Rank rank = Rank.getRankByIdentifier(user.getPrimaryGroup());
        return rank == null ? Rank.DEFAULT : rank;
    }

}
