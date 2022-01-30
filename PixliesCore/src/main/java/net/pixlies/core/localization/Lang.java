package net.pixlies.core.localization;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Lang {

    BAN_MESSAGE("", new HashMap<>()),
    BANNED_PLAYER_TRIED_TO_JOIN(Lang.EARTH, new HashMap<>()),
    PLAYER_PERMANENTLY_BANNED(Lang.EARTH, new HashMap<>()),
    PLAYER_TEMPORARILY_BANNED(Lang.EARTH, new HashMap<>()),
    PLAYER_PERMANENTLY_MUTED(Lang.EARTH, new HashMap<>()),
    PLAYER_TEMPORARILY_MUTED(Lang.EARTH, new HashMap<>()),
    MUTE_MESSAGE("", new HashMap<>()),
    MUTED_PLAYER_TRIED_TO_TALK(Lang.EARTH, new HashMap<>()),

    CHAT_CLEARED(Lang.EARTH, new HashMap<>()),
    CHAT_MUTED(Lang.EARTH, new HashMap<>()),
    CHAT_UNMUTED(Lang.EARTH, new HashMap<>()),

    SWEAR_FILTER_ON(Lang.EARTH, new HashMap<>()),
    SWEAR_FILTER_OFF(Lang.EARTH, new HashMap<>()),
    PLAYER_TRIED_TO_SWEAR(Lang.EARTH, new HashMap<>()),

    ADDED_BLOCKED_WORD(Lang.EARTH, new HashMap<>()),
    REMOVED_BLOCKED_WORD(Lang.EARTH, new HashMap<>()),
    PLAYER_TRIED_BLOCKED_WORD(Lang.EARTH, new HashMap<>()),

    SLOWMODE_SET(Lang.EARTH, new HashMap<>()),
    SLOWMODE_OFF(Lang.EARTH, new HashMap<>()),
    SLOWMODE_MESSAGE(Lang.EARTH, new HashMap<>()),

    PLAYER_DOESNT_EXIST(Lang.EARTH, new HashMap<>()),

    // STAFF
    STAFF_GOD_ON(Lang.EARTH, new HashMap<>()),
    STAFF_GOD_OFF(Lang.EARTH, new HashMap<>()),

    STAFF_WORLD_TELEPORT(Lang.EARTH, new HashMap<>()),

    STAFFMODE_SETTING_TURNED_ON(Lang.EARTH, new HashMap<>()),
    STAFFMODE_SETTING_TURNED_OFF(Lang.EARTH, new HashMap<>()),

    // MODERATION
    MODERATION_INVSEE_DISALLOW_SELF(Lang.EARTH, new HashMap<>()),

    MODERATION_GLOBAL_PVP_ON(Lang.EARTH, new HashMap<>()),
    MODERATION_GLOBAL_PVP_OFF(Lang.EARTH, new HashMap<>()),
    PVP_OFF_PLAYER_TRIED_TO_PVP(Lang.EARTH, new HashMap<>()),

    MODERATION_ISSUED_KICKALL(Lang.EARTH, new HashMap<>()),
    MODERATION_KICKALL_MESSAGE(Lang.EARTH, new HashMap<>()),

    // COSMETICS
    MODERATION_GOD_ON(Lang.EARTH, new HashMap<>()),
    MODERATION_GOD_OFF(Lang.EARTH, new HashMap<>()),

    COSMETICS_HEALED_SELF(Lang.EARTH, new HashMap<>()),
    COSMETICS_HEALED_OTHERS(Lang.EARTH, new HashMap<>()),

    COSMETICS_FEED_SELF(Lang.EARTH, new HashMap<>()),
    COSMETICS_FEED_OTHERS(Lang.EARTH, new HashMap<>()),

    COSMETICS_NIGHTVISION_ON(Lang.EARTH, new HashMap<>()),
    COSMETICS_NIGHTVISION_OFF(Lang.EARTH, new HashMap<>()),
    COSMETICS_NIGHTVISION_ON_OTHER(Lang.EARTH, new HashMap<>()),
    COSMETICS_NIGHTVISION_OFF_OTHER(Lang.EARTH, new HashMap<>()),

    COSMETICS_REPAIR_ITEM(Lang.EARTH, new HashMap<>()),
    COSMETICS_CANNOT_REPAIR_ITEM(Lang.EARTH, new HashMap<>()),
    COSMETICS_ANNOUNCE_PLAYER_SUICIDE(Lang.EARTH, new HashMap<>()),

    // PLAYER
    PLAYER_MESSAGE_FORMAT_TO(Lang.EARTH, new HashMap<>()),
    PLAYER_MESSAGE_FORMAT_FROM(Lang.EARTH, new HashMap<>()),
    PLAYER_MESSAGE_NOONE_TO_MESSAGE(Lang.EARTH, new HashMap<>()),

    PLAYER_SUICIDE(Lang.EARTH, new HashMap<>()),

    // OTHER
    MUST_BE_A_PLAYER(Lang.EARTH, new HashMap<>()),
    INVALID_COMMAND_SYNTAX(Lang.EARTH, new HashMap<>());

    private final String PREFIX;
    private @Getter Map<String, String> languages;

    private static @Getter final Map<String, ItemStack> items = new HashMap<>();

    public static final String EARTH = "§x§2§E§D§C§3§E§lE§x§3§0§C§A§3§E§lA§x§3§1§B§F§3§E§lR§x§3§8§B§2§4§3§lT§x§3§7§A§3§4§1§lH §8| ";
    public static final String NATION = "§x§2§F§D§1§E§5§lN§x§3§4§C§6§D§8§lA§x§3§7§B§A§C§A§lT§x§3§5§B§0§B§F§lI§x§3§3§A§3§B§0§lO§x§3§2§9§9§A§5§lN §8| ";
    public static final String DISCORD = "§3§lDISCORD §8| ";
    public static final String WAR = "§x§f§f§2§b§2§bW§x§e§b§2§1§2§1A§x§d§9§1§a§1§aR §8| ";

    Lang(String PREFIX, Map<String, String> languages) {
        this.PREFIX = PREFIX;
        this.languages = languages;
    }

    public String get(CommandSender sender) {
        if (languages == null) return "";
        try {
            if (sender instanceof Player player) {
                User user = User.get(player.getUniqueId());
                return languages.containsKey(user.getLang()) ? PREFIX + ChatColor.translateAlternateColorCodes('&', languages.get(user.getLang())) : PREFIX + ChatColor.translateAlternateColorCodes('&', languages.get("ENG"));
            }
        } catch (Exception e) {
            return PREFIX + languages.get("ENG").replace("&", "§");
        }
        return PREFIX + languages.get("ENG").replace("&", "§");
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

    public static void init() {
        int loaded = 0;
        File folder = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/languages/");
        if (!folder.exists()) {
            System.out.println("Couldn't load languages. Folder doesn't exist.");
            return;
        }
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
        Main.getInstance().getLogger().info("§7Added §b" + loaded + " §7languages.");
    }

}
