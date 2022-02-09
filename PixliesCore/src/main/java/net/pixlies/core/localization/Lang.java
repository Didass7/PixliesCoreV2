package net.pixlies.core.localization;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Lang {

    BAN_MESSAGE("", new HashMap<>()),
    BANNED_PLAYER_TRIED_TO_JOIN(Lang.PIXLIES, new HashMap<>()),
    PLAYER_PERMANENTLY_BANNED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_TEMPORARILY_BANNED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_PERMANENTLY_MUTED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_TEMPORARILY_MUTED(Lang.PIXLIES, new HashMap<>()),
    MUTE_MESSAGE("", new HashMap<>()),
    MUTED_PLAYER_TRIED_TO_TALK(Lang.PIXLIES, new HashMap<>()),

    NO_ITEMS_FOUND_ON_PLAYER(Lang.PIXLIES, new HashMap<>()),

    CHAT_CLEARED(Lang.PIXLIES, new HashMap<>()),
    CHAT_MUTED(Lang.PIXLIES, new HashMap<>()),
    CHAT_UNMUTED(Lang.PIXLIES, new HashMap<>()),
    CHAT_MUTED_FORMAT(Lang.PIXLIES, new HashMap<>()),

    SWEAR_FILTER_ON(Lang.PIXLIES, new HashMap<>()),
    SWEAR_FILTER_OFF(Lang.PIXLIES, new HashMap<>()),
    PLAYER_TRIED_TO_SWEAR(Lang.PIXLIES, new HashMap<>()),

    ADDED_BLOCKED_WORD(Lang.PIXLIES, new HashMap<>()),
    REMOVED_BLOCKED_WORD(Lang.PIXLIES, new HashMap<>()),
    PLAYER_TRIED_BLOCKED_WORD(Lang.PIXLIES, new HashMap<>()),

    SLOWMODE_SET(Lang.PIXLIES, new HashMap<>()),
    SLOWMODE_OFF(Lang.PIXLIES, new HashMap<>()),
    SLOWMODE_MESSAGE(Lang.PIXLIES, new HashMap<>()),

    PLAYER_DOESNT_EXIST(Lang.PIXLIES, new HashMap<>()),

    CANNOT_USE_ON_SELF(Lang.PIXLIES, new HashMap<>()),
    CANNOT_EDIT_ITEM(Lang.PIXLIES, new HashMap<>()),

    PLAYER_TELEPORT_NO_PLAYERS(Lang.PIXLIES, new HashMap<>()),

    LOCATION_FORMAT("", new HashMap<>()),

    // STAFF
    STAFF_GOD_ON(Lang.PIXLIES, new HashMap<>()),
    STAFF_GOD_OFF(Lang.PIXLIES, new HashMap<>()),

    STAFF_WORLD_TELEPORT(Lang.PIXLIES, new HashMap<>()),

    STAFF_RANDOM_TELEPORT(Lang.PIXLIES, new HashMap<>()),
    STAFF_RANDOM_TELEPORT_OTHER(Lang.PIXLIES, new HashMap<>()),

    STAFF_STAFFMODE_ON(Lang.PIXLIES, new HashMap<>()),
    STAFF_STAFFMODE_OFF(Lang.PIXLIES, new HashMap<>()),
    STAFF_STAFFMODE_SETTING_ON(Lang.PIXLIES, new HashMap<>()),
    STAFF_STAFFMODE_SETTING_OFF(Lang.PIXLIES, new HashMap<>()),
    STAFF_STAFFMODE_ON_OTHER(Lang.PIXLIES, new HashMap<>()),
    STAFF_STAFFMODE_OFF_OTHER(Lang.PIXLIES, new HashMap<>()),

    STAFF_GAMEMODE_CHANGED_SELF(Lang.PIXLIES, new HashMap<>()),
    STAFF_GAMEMODE_CHANGED_OTHERS(Lang.PIXLIES, new HashMap<>()),

    STAFF_BREAK_BLOCK_NOT_FOUND(Lang.PIXLIES, new HashMap<>()),
    STAFF_BREAK_DONE(Lang.PIXLIES, new HashMap<>()),

    STAFF_BROADCAST_MESSAGE(Lang.BROADCAST, new HashMap<>()),

    STAFF_TELEPORT_SELF_TO_TARGET(Lang.PIXLIES, new HashMap<>()),
    STAFF_TELEPORT_PLAYER_TO_TARGET(Lang.PIXLIES, new HashMap<>()),

    TPHERE_MESSAGE_SENDER(Lang.PIXLIES, new HashMap<>()),
    TPHERE_MESSAGE_TARGET(Lang.PIXLIES, new HashMap<>()),
    TPASK_MESSAGE_SENDER(Lang.PIXLIES, new HashMap<>()),
    TPASK_MESSAGE_TARGET(Lang.PIXLIES, new HashMap<>()),
    TPASK_ACCEPTED_MESSAGE_SENDER(Lang.PIXLIES, new HashMap<>()),
    TPASK_ACCEPTED_MESSAGE_TARGET(Lang.PIXLIES, new HashMap<>()),
    TPACCEPT_NOBODY(Lang.PIXLIES, new HashMap<>()),
    TPACCEPT_PLAYER_NOT_ONLINE(Lang.PIXLIES, new HashMap<>()),
    TPALL_BROADCAST(Lang.PIXLIES, new HashMap<>()),
    RETURNED_TO_PREVIOUS_LOCATION(Lang.PIXLIES, new HashMap<>()),
    TPBACK_NO_LOCATION(Lang.PIXLIES, new HashMap<>()),

    // MODERATION
    MODERATION_GLOBAL_PVP_ON(Lang.PIXLIES, new HashMap<>()),
    MODERATION_GLOBAL_PVP_OFF(Lang.PIXLIES, new HashMap<>()),
    PVP_OFF_PLAYER_TRIED_TO_PVP(Lang.PIXLIES, new HashMap<>()),

    MODERATION_ISSUED_KICKALL(Lang.PIXLIES, new HashMap<>()),
    MODERATION_KICKALL_MESSAGE(Lang.PIXLIES, new HashMap<>()),

    MODERATION_VANISH_ON(Lang.PIXLIES, new HashMap<>()),
    MODERATION_VANISH_OFF(Lang.PIXLIES, new HashMap<>()),
    MODERATION_VANISH_ON_OTHER(Lang.PIXLIES, new HashMap<>()),
    MODERATION_VANISH_OFF_OTHER(Lang.PIXLIES, new HashMap<>()),
    MODERATION_VANISH_NO_ONE_CAN_SEE("", new HashMap<>()),

    MODERATION_COMMANDSPY_ON(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_OFF(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_ON_OTHER(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_OFF_OTHER(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_FORMAT(Lang.COMMAND, new HashMap<>()),

    // COSMETICS
    COSMETICS_HEALED_SELF(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_HEALED_OTHERS(Lang.PIXLIES, new HashMap<>()),

    COSMETICS_FEED_SELF(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_FEED_OTHERS(Lang.PIXLIES, new HashMap<>()),

    COSMETICS_NIGHTVISION_ON(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_NIGHTVISION_OFF(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_NIGHTVISION_ON_OTHER(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_NIGHTVISION_OFF_OTHER(Lang.PIXLIES, new HashMap<>()),

    COSMETICS_REPAIR_ITEM(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_ALREADY_REPAIRED(Lang.PIXLIES, new HashMap<>()),

    COSMETICS_ANNOUNCE_PLAYER_SUICIDE(Lang.PIXLIES, new HashMap<>()),

    COSMETICS_RENAME_ITEM(Lang.PIXLIES, new HashMap<>()),

    COSMETICS_CANNOT_SIT_HERE(Lang.PIXLIES, new HashMap<>()),

    COSMETICS_NICKNAME(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_NICKNAME_OTHER(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_CANNOT_NICKNAME(Lang.PIXLIES, new HashMap<>()),

    COSMETICS_HAT_CANNOT_SET(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_HAT_SET(Lang.PIXLIES, new HashMap<>()),

    // PLAYER
    PLAYER_MESSAGE_FORMAT_TO("", new HashMap<>()),
    PLAYER_MESSAGE_FORMAT_FROM("", new HashMap<>()),
    PLAYER_MESSAGE_NOONE_TO_MESSAGE(Lang.PIXLIES, new HashMap<>()),
    PLAYER_MESSAGE_SOCIALSPY(Lang.PM, new HashMap<>()),

    PLAYER_SUICIDE(Lang.PIXLIES, new HashMap<>()),
    CHEST_LOCKED(Lang.PIXLIES, new HashMap<>()),
    CHEST_UNLOCKED(Lang.PIXLIES, new HashMap<>()),
    CHEST_BELONGS_TO_OTHER(Lang.PIXLIES, new HashMap<>()),
    MUST_BE_FACING_A_CHEST(Lang.PIXLIES, new HashMap<>()),

    PLAYER_CLEAR(Lang.PIXLIES, new HashMap<>()),

    PLAYER_RULES_MESSAGE(Lang.PIXLIES, new HashMap<>()),
    PLAYER_DISCORD_MESSAGE(Lang.PIXLIES, new HashMap<>()),

    PLAYER_AFK_ON(Lang.PIXLIES, new HashMap<>()),
    PLAYER_AFK_OFF(Lang.PIXLIES, new HashMap<>()),
    PLAYER_AFK_ON_BROADCAST(Lang.PIXLIES, new HashMap<>()),
    PLAYER_AFK_OFF_BROADCAST(Lang.PIXLIES, new HashMap<>()),

    // LOBBY
    LOBBY_BUILDMODE_TOGGLE(Lang.PIXLIES, new HashMap<>()),

    // NATIONS
    NATION_FORMED(Lang.NATION, new HashMap<>()),
    ALREADY_IN_NATION(Lang.NATION, new HashMap<>()),

    // OTHER
    MUST_BE_A_PLAYER(Lang.PIXLIES, new HashMap<>()),
    INVALID_COMMAND_SYNTAX(Lang.PIXLIES, new HashMap<>());

    private final String PREFIX;
    private @Getter Map<String, String> languages;

    public static final String PIXLIES = "§x§4§e§d§e§d§b§lP§x§4§8§c§b§c§8§lI§x§4§2§b§7§b§5§lX§x§3§c§a§4§a§2§lL§x§3§5§9§0§8§f§lI§x§2§f§7§d§7§c§lE§x§2§9§6§9§6§9§lS §8| ";
    public static final String EARTH = "§x§2§E§D§C§3§E§lE§x§3§0§C§A§3§E§lA§x§3§1§B§F§3§E§lR§x§3§8§B§2§4§3§lT§x§3§7§A§3§4§1§lH §8| ";
    public static final String NATION = "§x§2§F§D§1§E§5§lN§x§3§4§C§6§D§8§lA§x§3§7§B§A§C§A§lT§x§3§5§B§0§B§F§lI§x§3§3§A§3§B§0§lO§x§3§2§9§9§A§5§lN §8| ";
    public static final String DISCORD = "§3DISCORD §8| ";
    public static final String WAR = "§x§f§f§2§b§2§bW§x§e§b§2§1§2§1A§x§d§9§1§a§1§aR §8| ";
    public static final String PM = "§x§a§8§1§1§c§eP§x§a§4§0§8§a§4M §8| ";
    public static final String COMMAND = "§x§f§f§a§a§0§0C§x§d§4§8§d§0§0§M§x§a§9§6§f§0§0§D §8| ";
    public static final String BROADCAST = "&x&f&f&5&5&5&5&lB&x&f&4&4&a&4&a&lR&x&e&a&4&0&4&0&lO&x&d&f&3&5&3&5&lA&x&d&5&2&b&2&b&lD&x&c&a&2&0&2&0&lC&x&b&f&1&5&1&5&lA&x&b&5&0&b&0&b&lS&x&a&a&0&0&0&0&lT §8| ";

    Lang(String PREFIX, Map<String, String> languages) {
        this.PREFIX = PREFIX;
        this.languages = languages;
    }

    public String get(CommandSender sender) {
        if (languages == null) return "";
        try {
            if (sender instanceof Player player) {
                User user = User.get(player.getUniqueId());
                String lang = user.getLang();
                if (languages.containsKey(lang)) {
                    return PREFIX + CC.format(languages.get(user.getLang()));
                } else {
                    return PREFIX + CC.format(languages.get("ENG"));
                }
            }
        } catch (Exception ignored) {

        }
        return PREFIX + CC.format(languages.get("ENG"));
    }

    public String getRaw(String language) {
        return languages.get(language).replace("&", "§");
    }

    public boolean send(CommandSender sender) {
        if (sender == null)
            return false;
        sender.sendMessage(get(sender));
        return true;
    }

    public boolean sendWithLangName(CommandSender sender, String langName) {
        sender.sendMessage(PREFIX + languages.get(langName).replace("&", "§"));
        return true;
    }

    public void broadcast(Map<String, String> placeholders) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String s = get(player);
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                s = s.replace(entry.getKey(), entry.getValue());
            }
            player.sendMessage(s);
        }
    }

    public void broadcast(Map<String, String> placeholders, String permission) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(permission)) continue;
            String s = get(player);
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                s = s.replace(entry.getKey(), entry.getValue());
            }
            player.sendMessage(s);
        }
    }

    public void broadcast(String... placeholders) {
        for (Player player : Bukkit.getOnlinePlayers())
            send(player, placeholders);
    }

    public void broadcastPermission(String permission, String... placeholders) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.hasPermission(permission))
                send(player, placeholders);
    }

    public void kickPlayer(CommandSender sender) {
        Player player = (Player) sender;
        player.kick(Component.text(get(sender)));
    }

    public void kickPlayer(CommandSender sender, String... placeholders) {
        Player player = (Player) sender;
        String send = get(sender);
        for (String s : placeholders) {
            String[] pSplit = s.split(";");
            send = send.replace(pSplit[0], pSplit[1]);
        }
        player.kick(Component.text(send));
    }

    public void setLanguage(Map<String, String> languages) {
        this.languages = languages;
    }

    /**
     * SEND MESSAGE TO PLAYER WITH PLACEHOLDERS
     * @param sender the command sender to send to
     * @param placeholders get declared by writing TOREPLACE;REPLACED
     * @return if the action was successful
     */
    public boolean send(CommandSender sender, String... placeholders) {
        if (sender == null) return false;
        String send = get(sender);
        for (String s : placeholders) {
            String[] pSplit = s.split(";");
            send = send.replace(pSplit[0], pSplit[1]);
        }
        sender.sendMessage(send);
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // folder.mkdirs() is safe to ignore
    public static void init() {
        int loaded = 0;
        File folder = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/languages/");
        if (!folder.exists()) folder.mkdirs();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (!file.getName().endsWith(".yml"))
                continue;
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            String langName = file.getName().replace("LANG_", "").replace(".yml", "").toUpperCase();
            for (Lang l : values()) {
                Map<String, String> map = new HashMap<>(l.languages);
                map.put(langName, cfg.getString(l.name()));
                l.setLanguage(map);
            }
            loaded++;
        }
        Main.getInstance().getLogger().info("§7Loaded §b" + loaded + " §7languages.");
    }

}
