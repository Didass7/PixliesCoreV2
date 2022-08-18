package net.pixlies.core.localization;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
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

    FETCHING(Lang.PIXLIES, new HashMap<>()),

    BAN_MESSAGE("", new HashMap<>()),
    KICK_MESSAGE("", new HashMap<>()),
    BLACKLIST_MESSAGE("", new HashMap<>()),

    BANNED_PLAYER_TRIED_TO_JOIN(Lang.PIXLIES, new HashMap<>()),

    PLAYER_PERMANENTLY_BANNED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_UNBANNED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_TEMPORARILY_BANNED(Lang.PIXLIES, new HashMap<>()),

    PLAYER_BLACKLISTED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_UNBLACKLISTED(Lang.PIXLIES, new HashMap<>()),

    PLAYER_KICKED(Lang.PIXLIES, new HashMap<>()),

    PLAYER_PERMANENTLY_MUTED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_UNMUTED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_TEMPORARILY_MUTED(Lang.PIXLIES, new HashMap<>()),

    MUTE_MESSAGE("", new HashMap<>()),
    MUTED_PLAYER_TRIED_TO_TALK(Lang.PIXLIES, new HashMap<>()),

    PLAYER_NOT_MUTED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_NOT_BLACKLISTED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_NOT_BANNED(Lang.PIXLIES, new HashMap<>()),

    PLAYER_ALREADY_MUTED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_ALREADY_BLACKLISTED(Lang.PIXLIES, new HashMap<>()),
    PLAYER_ALREADY_BANNED(Lang.PIXLIES, new HashMap<>()),

    SETTING_CHANGED(Lang.PIXLIES, new HashMap<>()),

    NOT_A_NUMBER(Lang.PIXLIES, new HashMap<>()),

    NO_ITEMS_FOUND_ON_PLAYER(Lang.PIXLIES, new HashMap<>()),

    CHAT_CLEARED(Lang.PIXLIES, new HashMap<>()),
    CHAT_MUTED(Lang.PIXLIES, new HashMap<>()),
    CHAT_UNMUTED(Lang.PIXLIES, new HashMap<>()),
    CHAT_MUTED_FORMAT(Lang.PIXLIES, new HashMap<>()),

    SLOWMODE_SET(Lang.PIXLIES, new HashMap<>()),
    SLOWMODE_GET(Lang.PIXLIES, new HashMap<>()),
    SLOWMODE_OFF(Lang.PIXLIES, new HashMap<>()),
    SLOWMODE_MESSAGE(Lang.PIXLIES, new HashMap<>()),

    PLAYER_DOESNT_EXIST(Lang.PIXLIES, new HashMap<>()),

    NOT_HOLDING_ANYTHING(Lang.PIXLIES, new HashMap<>()),

    CANNOT_USE_ON_SELF(Lang.PIXLIES, new HashMap<>()),
    CANNOT_EDIT_ITEM(Lang.PIXLIES, new HashMap<>()),

    PLAYER_TELEPORT_NO_PLAYERS(Lang.PIXLIES, new HashMap<>()),

    LOCATION_FORMAT("", new HashMap<>()),

    // ADMIN
    WIPED_USERS(Lang.PIXLIES, new HashMap<>()),

    // STAFF
    STAFF_GOD_ON(Lang.PIXLIES, new HashMap<>()),
    STAFF_GOD_OFF(Lang.PIXLIES, new HashMap<>()),

    STAFF_RANDOM_TELEPORT(Lang.PIXLIES, new HashMap<>()),
    STAFF_RANDOM_TELEPORT_OTHER(Lang.PIXLIES, new HashMap<>()),

    STAFF_STAFFMODE_ON(Lang.PIXLIES, new HashMap<>()),
    STAFF_STAFFMODE_OFF(Lang.PIXLIES, new HashMap<>()),
    STAFF_STAFFMODE_ON_OTHER(Lang.PIXLIES, new HashMap<>()),
    STAFF_STAFFMODE_OFF_OTHER(Lang.PIXLIES, new HashMap<>()),

    STAFF_BYPASS_ON(Lang.PIXLIES, new HashMap<>()),
    STAFF_BYPASS_OFF(Lang.PIXLIES, new HashMap<>()),
    STAFF_BYPASS_ON_OTHER(Lang.PIXLIES, new HashMap<>()),
    STAFF_BYPASS_OFF_OTHER(Lang.PIXLIES, new HashMap<>()),
    STAFF_BYPASS_ACTIONBAR("", new HashMap<>()),

    STAFF_GAMEMODE_CHANGED_SELF(Lang.PIXLIES, new HashMap<>()),
    STAFF_GAMEMODE_CHANGED_OTHERS(Lang.PIXLIES, new HashMap<>()),

    STAFF_BREAK_BLOCK_NOT_FOUND(Lang.PIXLIES, new HashMap<>()),
    STAFF_BREAK_DONE(Lang.PIXLIES, new HashMap<>()),

    STAFF_BROADCAST_MESSAGE(Lang.BROADCAST, new HashMap<>()),

    STAFF_TELEPORT_SELF_TO_TARGET(Lang.PIXLIES, new HashMap<>()),
    STAFF_TELEPORT_PLAYER_TO_TARGET(Lang.PIXLIES, new HashMap<>()),

    STAFF_MODELDATA_SET(Lang.PIXLIES, new HashMap<>()),
    STAFF_MODELDATA_GET(Lang.PIXLIES, new HashMap<>()),

    STAFF_SPEED_FLY_GET(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_FLY_SET(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_FLY_SET_OTHER(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_FLY_GET_OTHER(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_WALK_GET(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_WALK_SET(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_WALK_GET_OTHER(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_WALK_SET_OTHER(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_RESET(Lang.PIXLIES, new HashMap<>()),
    STAFF_SPEED_RESET_OTHER(Lang.PIXLIES, new HashMap<>()),

    STAFF_SUDO_COMMAND(Lang.PIXLIES, new HashMap<>()),
    STAFF_SUDO_CHAT(Lang.PIXLIES, new HashMap<>()),
    STAFF_SUDO_CANNOT(Lang.PIXLIES, new HashMap<>()),

    STAFF_CHAT_ENABLED(Lang.STAFF, new HashMap<>()),
    STAFF_CHAT_DISABLED(Lang.STAFF, new HashMap<>()),
    STAFF_CHAT_FORMAT(Lang.STAFF, new HashMap<>()),

    STAFF_SERVER_JOIN_FORMAT(Lang.STAFF, new HashMap<>()),
    STAFF_SERVER_QUIT_FORMAT(Lang.STAFF, new HashMap<>()),

    STAFF_TIME_SET(Lang.PIXLIES, new HashMap<>()),

    STAFF_SETSPAWN(Lang.PIXLIES, new HashMap<>()),

    // TELEPORTATION
    TPHERE_MESSAGE_SENDER(Lang.PIXLIES, new HashMap<>()),
    TPHERE_MESSAGE_TARGET(Lang.PIXLIES, new HashMap<>()),
    TPASK_MESSAGE_SENDER(Lang.PIXLIES, new HashMap<>()),
    TPASK_MESSAGE_TARGET(Lang.PIXLIES, new HashMap<>()),
    TPASK_ACCEPTED_MESSAGE_SENDER(Lang.PIXLIES, new HashMap<>()),
    TPASK_ACCEPTED_MESSAGE_TARGET(Lang.PIXLIES, new HashMap<>()),
    TPACCEPT_NOBODY(Lang.PIXLIES, new HashMap<>()),
    TPACCEPT_PLAYER_NOT_ONLINE(Lang.PIXLIES, new HashMap<>()),
    RETURNED_TO_PREVIOUS_LOCATION(Lang.PIXLIES, new HashMap<>()),
    TPBACK_NO_LOCATION(Lang.PIXLIES, new HashMap<>()),
    TP_CANCEL(Lang.PIXLIES, new HashMap<>()),

    // COMBAT
    COMBAT_TELEPORT(Lang.PIXLIES, new HashMap<>()),
    COMBAT_HIT(Lang.PIXLIES, new HashMap<>()),

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
    MODERATION_VANISH_JOIN_VANISH_ENABLED(Lang.PIXLIES, new HashMap<>()),
    MODERATION_VANISH_JOIN_VANISH_DISABLED(Lang.PIXLIES, new HashMap<>()),

    MODERATION_COMMANDSPY_ON(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_OFF(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_ON_OTHER(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_OFF_OTHER(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_FORMAT(Lang.CMD, new HashMap<>()),

    MODERATION_FREEZE_FREEZE(Lang.PIXLIES, new HashMap<>()),
    MODERATION_FREEZE_UNFREEZE(Lang.PIXLIES, new HashMap<>()),
    MODERATION_CANNOT_FREEZE(Lang.PIXLIES, new HashMap<>()),
    MODERATION_FREEZE_ON(Lang.PIXLIES, new HashMap<>()),
    MODERATION_FREEZE_OFF(Lang.PIXLIES, new HashMap<>()),
    MODERATION_FREEZE_MESSAGE(Lang.PIXLIES, new HashMap<>()),
    MODERATION_FREEZE_ACTIONBAR("", new HashMap<>()),

    MODERATION_CANNOT_KICK(Lang.PIXLIES, new HashMap<>()),
    MODERATION_CANNOT_BAN(Lang.PIXLIES, new HashMap<>()),
    MODERATION_CANNOT_MUTE(Lang.PIXLIES, new HashMap<>()),
    MODERATION_BANSPY_ENABLED(Lang.PIXLIES, new HashMap<>()),
    MODERATION_BANSPY_DISABLED(Lang.PIXLIES, new HashMap<>()),

    MODERATION_SOCIALSPY_ENABLED(Lang.PIXLIES, new HashMap<>()),
    MODERATION_SOCIALSPY_DISABLED(Lang.PIXLIES, new HashMap<>()),

    MODERATION_MUTESPY_ENABLED(Lang.PIXLIES, new HashMap<>()),
    MODERATION_MUTESPY_DISABLED(Lang.PIXLIES, new HashMap<>()),

    MODERATION_COMMANDSPY_ENABLED(Lang.PIXLIES, new HashMap<>()),
    MODERATION_COMMANDSPY_DISABLED(Lang.PIXLIES, new HashMap<>()),

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
    COSMETICS_HAT_ALREADY_HAVE(Lang.PIXLIES, new HashMap<>()),
    COSMETICS_HAT_SET(Lang.PIXLIES, new HashMap<>()),

    // POLLS
    CREATING_POLL(Lang.POLL, new HashMap<>()),
    ENTER_POLL_OPTION(Lang.POLL, new HashMap<>()),
    HOW_TO_CANCEL_POLL(Lang.POLL, new HashMap<>()),
    POLL_CANCELLED(Lang.POLL, new HashMap<>()),
    ADDED_POLL_OPTION(Lang.POLL, new HashMap<>()),
    HOW_TO_END_POLL_CREATION(Lang.POLL, new HashMap<>()),
    ALREADY_CREATING_POLL(Lang.POLL, new HashMap<>()),

    POLL_DOES_NOT_EXIST(Lang.POLL, new HashMap<>()),
    POLL_VIEW_INFO(Lang.POLL, new HashMap<>()),
    POLL_LIST_INFO(Lang.POLL, new HashMap<>()),
    POLL_OPTION_DOES_NOT_EXIST(Lang.POLL, new HashMap<>()),
    REGISTERED_VOTE(Lang.POLL, new HashMap<>()),
    POLL_RESULTS_INFO(Lang.POLL, new HashMap<>()),
    POLL_SHOW_QUESTION(Lang.POLL, new HashMap<>()),

    // PLAYER
    PLAYER_MESSAGE_FORMAT_TO("", new HashMap<>()),
    PLAYER_MESSAGE_FORMAT_FROM("", new HashMap<>()),
    PLAYER_MESSAGE_NOONE_TO_MESSAGE(Lang.PIXLIES, new HashMap<>()),
    PLAYER_MESSAGE_SOCIALSPY(Lang.PM, new HashMap<>()),

    PLAYER_SUICIDE(Lang.PIXLIES, new HashMap<>()),

    PLAYER_CLEAR(Lang.PIXLIES, new HashMap<>()),

    PLAYER_RULES_MESSAGE(Lang.PIXLIES, new HashMap<>()),
    PLAYER_DISCORD_MESSAGE(Lang.PIXLIES, new HashMap<>()),

    PLAYER_AFK_ON(Lang.PIXLIES, new HashMap<>()),
    PLAYER_AFK_OFF(Lang.PIXLIES, new HashMap<>()),
    PLAYER_AFK_ON_BROADCAST(Lang.PIXLIES, new HashMap<>()),
    PLAYER_AFK_OFF_BROADCAST(Lang.PIXLIES, new HashMap<>()),

    PLAYER_PLAYTIME(Lang.PIXLIES, new HashMap<>()),
    PLAYER_PLAYTIME_OTHER(Lang.PIXLIES, new HashMap<>()),

    PLAYER_KISS(Lang.PIXLIES, new HashMap<>()),

    PLAYER_WARP(Lang.PIXLIES, new HashMap<>()),
    PLAYER_WARP_OTHER(Lang.PIXLIES, new HashMap<>()),

    PLAYER_BALANCE(Lang.PIXLIES, new HashMap<>()),
    PLAYER_BALANCE_OTHER(Lang.PIXLIES, new HashMap<>()),

    PLAYER_STATS("", new HashMap<>()),

    // LOBBY
    LOBBY_BUILDMODE_TOGGLE(Lang.PIXLIES, new HashMap<>());

    private final String PREFIX;
    private @Getter Map<String, String> languages;

    public static final String PIXLIES = "§x§4§e§d§e§d§b§lP§x§4§8§c§b§c§8§lI§x§4§2§b§7§b§5§lX§x§3§c§a§4§a§2§lL§x§3§5§9§0§8§f§lI§x§2§f§7§d§7§c§lE§x§2§9§6§9§6§9§lS§8 | §7";
    public static final String EARTH = "§x§2§E§D§C§3§E§lE§x§3§0§C§A§3§E§lA§x§3§1§B§F§3§E§lR§x§3§8§B§2§4§3§lT§x§3§7§A§3§4§1§lH§8 | §7";
    public static final String DISCORD = "§9§lDISCORD§8 | ";
    public static final String POLL = "§x§1§6§a§7§e§7§lP§x§1§a§8§d§c§7§lO§x§1§a§7§5§a§8§lL§x§1§6§5§d§8§a§lL §8| §7";
    public static final String WAR = "§x§f§f§2§b§2§bW§x§e§b§2§1§2§1A§x§d§9§1§a§1§aR§8 | §7";
    public static final String PM = "§x§a§8§1§1§c§e§lP§x§a§4§0§8§a§4§lM§8 | §7";
    public static final String CMD = "§x§f§f§a§a§0§0§lC§x§d§4§8§d§0§0§lM§x§a§9§6§f§0§0§lD§8 | ";
    public static final String BROADCAST = "§x§f§f§5§5§5§5§lB§x§f§4§4§a§4§a§lR§x§e§a§4§0§4§0§lO§x§d§f§3§5§3§5§lA§x§d§5§2§b§2§b§lD§x§c§a§2§0§2§0§lC§x§b§f§1§5§1§5§lA§x§b§5§0§b§0§b§lS§x§a§a§0§0§0§0§lT§8 | §7";
    public static final String STAFF = "§x§4§e§d§e§d§b§lS§x§4§5§c§1§b§f§lT§x§3§c§a§4§a§2§lA§x§3§2§8§6§8§6§lF§x§2§9§6§9§6§9§lF§8 | §7";

    Lang(String prefix, Map<String, String> languages) {
        this.PREFIX = prefix;
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

    public String get() {
        if (languages == null) return "";
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

        String senderString = get();
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            senderString = senderString.replace(entry.getKey(), entry.getValue());
        }
        Bukkit.getConsoleSender().sendMessage(senderString);
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

        String senderString = get();
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            senderString = senderString.replace(entry.getKey(), entry.getValue());
        }
        Bukkit.getConsoleSender().sendMessage(senderString);
    }

    public void broadcast(String... placeholders) {
        for (Player player : Bukkit.getOnlinePlayers())
            send(player, placeholders);
        send(Bukkit.getConsoleSender(), placeholders);
    }

    public void broadcastPermission(String permission, String... placeholders) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.hasPermission(permission))
                send(player, placeholders);
        send(Bukkit.getConsoleSender(), placeholders);
    }

    public void kickPlayer(CommandSender sender) {
        Player player = (Player) sender;
        player.kickPlayer(get(sender));
    }

    public void kickPlayer(CommandSender sender, String... placeholders) {
        Player player = (Player) sender;
        String send = get(sender);
        for (String s : placeholders) {
            String[] pSplit = s.split(";");
            send = send.replace(pSplit[0], pSplit[1]);
        }
        player.kickPlayer(send);
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
