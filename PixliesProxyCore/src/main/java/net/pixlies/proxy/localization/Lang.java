package net.pixlies.proxy.localization;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.utils.CC;

import java.util.HashMap;
import java.util.Map;

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

    public String get(CommandSender sender) {
//        if (languages == null) return "";
//        try {
//            if (sender instanceof Player player) {
//                User user = User.get(player.getUniqueId());
//                String lang = user.getLang();
//                if (languages.containsKey(lang)) {
//                    return PREFIX + CC.format(languages.get(user.getLang()));
//                } else {
//                    return PREFIX + CC.format(languages.get("ENG"));
//                }
//            }
//        } catch (Exception ignored) {
//
//        }
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
        for (ProxiedPlayer player : instance.getProxy().getPlayers()) {
            String s = get(player);
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                s = s.replace(entry.getKey(), entry.getValue());
            }
            player.sendMessage(s);
        }
    }

    public void broadcast(Map<String, String> placeholders, String permission) {
        for (ProxiedPlayer player : instance.getProxy().getPlayers()) {
            if (!player.hasPermission(permission)) continue;
            String s = get(player);
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                s = s.replace(entry.getKey(), entry.getValue());
            }
            player.sendMessage(s);
        }
    }

    public void broadcast(String... placeholders) {
        for (ProxiedPlayer player : instance.getProxy().getPlayers())
            send(player, placeholders);
    }

    public void broadcastPermission(String permission, String... placeholders) {
        for (ProxiedPlayer player : instance.getProxy().getPlayers())
            if (player.hasPermission(permission))
                send(player, placeholders);
    }

    public void kickPlayer(CommandSender sender) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        player.disconnect(get(sender));
    }

    public void kickPlayer(CommandSender sender, String... placeholders) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        String send = get(sender);
        for (String s : placeholders) {
            String[] pSplit = s.split(";");
            send = send.replace(pSplit[0], pSplit[1]);
        }
        player.disconnect(send);
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
