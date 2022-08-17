package net.pixlies.proxy.localization;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.utils.CC;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Lang for proxy
 * @author MickMMars
 * @author Dynmie
 */
public enum Lang {

    // PLAYER
    PLAYER_SERVER_CONNECTING(Lang.PIXLIES),
    PLAYER_SERVER_ALREADY_CONNECTED(Lang.PIXLIES),
    PLAYER_SERVER_CONNECTING_OTHER(Lang.PIXLIES),
    PLAYER_SERVER_NOT_EXIST(Lang.PIXLIES),
    PLAYER_SERVER_CANNOT_CONNECT(Lang.PIXLIES),
    PLAYER_SERVER_CANNOT_CONNECT_OTHER(Lang.PIXLIES),

    // QUEUE,
    PLAYER_QUEUE_NOT_IN(Lang.PIXLIES),
    PLAYER_QUEUE_ALREADY_IN(Lang.PIXLIES),
    PLAYER_QUEUE_NO_PERMISSION(Lang.PIXLIES),
    PLAYER_QUEUE_NOT_EXIST(Lang.PIXLIES),
    PLAYER_QUEUE_JOIN(Lang.PIXLIES),
    PLAYER_QUEUE_LEAVE(Lang.PIXLIES),
    PLAYER_QUEUE_UPDATE_SERVER_PAUSED_MESSAGE(Lang.PIXLIES),
    PLAYER_QUEUE_UPDATE_MESSAGE(Lang.PIXLIES),

    PLAYER_QUEUE_SETTINGS_COMMAND_PAUSE_ENABLED(Lang.PIXLIES),
    PLAYER_QUEUE_SETTINGS_COMMAND_PAUSE_DISABLED(Lang.PIXLIES);

    private static final PixliesProxy instance = PixliesProxy.getInstance();

    private final String prefix;
    private @Getter Map<String, String> languages = new HashMap<>();

    public static final String PIXLIES = "§x§4§e§d§e§d§b§lP§x§4§8§c§b§c§8§lI§x§4§2§b§7§b§5§lX§x§3§c§a§4§a§2§lL§x§3§5§9§0§8§f§lI§x§2§f§7§d§7§c§lE§x§2§9§6§9§6§9§lS §8| §7";
    public static final String STAFF = "§x§0§0§a§a§a§a§lS§x§0§0§8§0§a§a§lT§x§0§0§5§5§a§a§lA§x§0§0§2§b§a§a§lF§x§0§0§0§0§a§a§lF §8| §7";

    Lang(String prefix) {
        this.prefix = prefix;
    }

    public String get(String lang) {
        if (languages == null) return "";
        if (languages.isEmpty()) return "";
        return prefix + getRaw(lang);
    }

    public String getRaw(String language) {
        if (languages.get(language) == null) return get();
        return CC.format(languages.get(language));
    }

    /**
     * Shortened get.
     * @author Dynmie
     * @return message with default ENG locale.
     */
    public String get() {
        if (languages == null) return "";
        if (languages.isEmpty()) return "";
        return prefix + getRaw("ENG");
    }

    public void send(@NotNull CommandSender sender) {
        sender.sendMessage(get());
    }
    public void broadcast(@NotNull String... placeholders) {
        String message = replacePlaceholders(placeholders);
        instance.getProxy().broadcast(message);
    }

    public void broadcastPermission(@NotNull String permission, @NotNull String... placeholders) {
        for (ProxiedPlayer player : instance.getProxy().getPlayers())
            if (player.hasPermission(permission))
                send(player, placeholders);
        instance.getProxy().getConsole().sendMessage(get());
    }

    public void kickPlayer(@NotNull ProxiedPlayer player) {
        player.disconnect(get());
    }

    public void kickPlayer(@NotNull ProxiedPlayer player, @NotNull String... placeholders) {
        String send = replacePlaceholders(placeholders);
        player.disconnect(send);
    }

    public void setLanguage(@NotNull Map<String, String> languages) {
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
        String send = replacePlaceholders(placeholders);
        sender.sendMessage(send);
        return true;
    }

    /**
     * Replace placeholders, shortened way.
     * @author Dynmie
     * @param placeholders the placeholders, with ";" split
     * @return message with placeholders replaced
     */
    private String replacePlaceholders(String... placeholders) {
        String toReturn = get();
        for (String string : placeholders) {
            String[] split = string.split(";");
            String placeholder = split[0] == null ? "" : split[0];
            String replacement = split[1] == null ? "" : split[1];
            toReturn = toReturn.replace(placeholder, replacement);
        }
        return toReturn;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // folder.mkdirs() is safe to ignore
    public static void init() {

        int loaded = 0;
        File folder = new File(instance.getDataFolder().getAbsolutePath() + "/languages");
        if (!folder.exists()) folder.mkdirs();

        File[] files = folder.listFiles();
        if (files == null) {
            instance.getLogger().severe("Failed to load language files.");
            return;
        }

        for (File file : files) {

            if (!file.getName().endsWith(".yml"))
                continue;

            Config config = new Config(file);
            String langName = file.getName()
                    .replace("LANG_", "")
                    .replace(".yml", "")
                    .toUpperCase();

            for (Lang lang : values()) {
                lang.languages.put(langName, config.getConfig().getString(lang.name()));
            }

            loaded++;
        }
        instance.getLogger().info("§7Loaded §b" + loaded + " §7languages.");
    }

}
