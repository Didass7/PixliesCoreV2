package net.pixlies.proxy.localization;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.utils.CC;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Lang for proxy
 * @author MickMMars
 * @author Dynmie
 */
public enum Lang {

    TEST("", new HashMap<>()); // TODO

    private static final Proxy instance = Proxy.getInstance();

    private final String PREFIX;
    private @Getter Map<String, String> languages;

    public static final String PIXLIES = "§x§4§e§d§e§d§b§lP§x§4§8§c§b§c§8§lI§x§4§2§b§7§b§5§lX§x§3§c§a§4§a§2§lL§x§3§5§9§0§8§f§lI§x§2§f§7§d§7§c§lE§x§2§9§6§9§6§9§lS §8| ";

    Lang(String PREFIX, Map<String, String> languages) {
        this.PREFIX = PREFIX;
        this.languages = languages;
    }

    public String get(CommandSender sender) { // TODO: User specified localization.
        if (languages == null) return "";
        if (languages.isEmpty()) return "";
        if (sender instanceof ProxiedPlayer player) {
            Locale locale = player.getLocale();
            String name = locale.getISO3Language();
            if (languages.containsKey(name)) {
                return PREFIX + getRaw(name);
            }
        }
        return get();
    }

    public String get(String lang) {
        return PREFIX + getRaw(lang);
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
        return PREFIX + getRaw("ENG");
    }

    public boolean send(CommandSender sender) {
        if (sender == null) return false;
        sender.sendMessage(new TextComponent(get(sender)));
        return true;
    }

    public boolean sendWithLangName(CommandSender sender, String langName) {
        sender.sendMessage(new TextComponent(get(langName)));
        return true;
    }

    public void broadcast(String... placeholders) {
        String message = replacePlaceholders(placeholders);
        instance.getProxy().broadcast(new TextComponent());
    }

    public void broadcastPermission(String permission, String... placeholders) {
        for (ProxiedPlayer player : instance.getProxy().getPlayers())
            if (player.hasPermission(permission))
                send(player, placeholders);
        instance.getProxy().getConsole().sendMessage(new TextComponent(get()));
    }

    public void kickPlayer(ProxiedPlayer player) {
        player.disconnect(new TextComponent(get(player)));
    }

    public void kickPlayer(ProxiedPlayer player, String... placeholders) {
        String send = replacePlaceholders(player, placeholders);
        player.disconnect(new TextComponent(send));
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
        String send = replacePlaceholders(sender, placeholders);
        sender.sendMessage(new TextComponent(send));
        return true;
    }

    /**
     * Replace placeholders.
     * @author Dynmie
     * @param sender the sender
     * @param placeholders the placeholders, with ";" split
     * @return message with placeholders replaced
     */
    private String replacePlaceholders(CommandSender sender, String... placeholders) {
        String toReturn = sender == null ? get() : get(sender);
        for (String string : placeholders) {
            String[] split = string.split(";");
            String placeholder = split[0] == null ? "" : split[0];
            String replacement = split[1] == null ? "" : split[1];
            toReturn = toReturn.replace(placeholder, replacement);
        }
        return toReturn;
    }

    /**
     * Replace placeholders, shortened way.
     * @author Dynmie
     * @param placeholders the placeholders, with ";" split
     * @return message with placeholders replaced
     */
    private String replacePlaceholders(String... placeholders) {
        return replacePlaceholders(null, placeholders);
    }

//    @SuppressWarnings("ResultOfMethodCallIgnored") // folder.mkdirs() is safe to ignore
//    public static void init() {
//        int loaded = 0;
//        File folder = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/languages/");
//        if (!folder.exists()) folder.mkdirs();
//        for (File file : Objects.requireNonNull(folder.listFiles())) {
//            if (!file.getName().endsWith(".yml"))
//                continue;
//            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
//            String langName = file.getName().replace("LANG_", "").replace(".yml", "").toUpperCase();
//            for (Lang l : values()) {
//                Map<String, String> map = new HashMap<>(l.languages);
//                map.put(langName, cfg.getString(l.name()));
//                l.setLanguage(map);
//            }
//            loaded++;
//        }
//        Main.getInstance().getLogger().info("§7Loaded §b" + loaded + " §7languages.");
//    }

}
