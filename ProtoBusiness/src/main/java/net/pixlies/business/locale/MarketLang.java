package net.pixlies.business.locale;

import lombok.Getter;
import net.pixlies.business.ProtoBusinesss;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import net.pixlies.nations.locale.NationsLang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public enum MarketLang {
    MARKET_OPEN(MarketLang.MARKET),
    MARKET_CLOSED(MarketLang.MARKET),
    MARKET_WAS_ALREADY_OPEN(MarketLang.MARKET),
    MARKET_WAS_ALREADY_CLOSED(MarketLang.MARKET),
    MARKET_IS_CLOSED(MarketLang.MARKET),
    MARKET_STATISTICS_RESET(MarketLang.MARKET),
    MARKET_PLAYER_STATISTICS_RESET(MarketLang.MARKET),
    NEW_ORDER_CREATED(MarketLang.MARKET),
    ORDER_CANCELLED(MarketLang.MARKET),
    ORDER_ITEMS_REFUNDED(MarketLang.MARKET),
    ORDER_COINS_REFUNDED(MarketLang.MARKET),
    ORDER_ITEMS_CLAIMED(MarketLang.MARKET),
    ORDER_COINS_CLAIMED(MarketLang.MARKET),
    ORDER_UNUSED_COINS_REFUNDED(MarketLang.MARKET),
    MARKET_NOT_A_VALID_PRICE(MarketLang.MARKET),
    MARKET_NOT_A_VALID_AMOUNT(MarketLang.MARKET),
    MARKET_NOT_ENOUGH_ITEMS(MarketLang.MARKET),
    MARKET_PLAYER_RESTRICTED_SENDER(MarketLang.MARKET),
    MARKET_PLAYER_RESTRICTED_TARGET(MarketLang.MARKET),
    MARKET_PLAYER_ALLOWED_SENDER(MarketLang.MARKET),
    MARKET_PLAYER_ALLOWED_TARGET(MarketLang.MARKET),
    MARKET_PLAYER_IS_RESTRICTED(MarketLang.MARKET),
    ITEM_NOT_ON_MARKET(MarketLang.MARKET),
    MARKET_NOTIFICATION(MarketLang.MARKET),
    MARKET_LOGON(MarketLang.MARKET),
    PRICE_SUMMARY(MarketLang.MARKET),
    PRICE_BEST_BUY(""),
    PRICE_BEST_SELL(""),
    PRICE_NOT_HOLDING_AN_ITEM(MarketLang.MARKET),
    
    ENTER_AN_AMOUNT(MarketLang.MARKET),
    ENTER_A_CUSTOM_PRICE(MarketLang.MARKET),
    THIS_IS_NOT_A_POSITIVE_INTEGER(MarketLang.MARKET),
    ENTERED_AN_AMOUNT(MarketLang.MARKET),
    ENTERED_A_PRICE(MarketLang.MARKET),
    
    // CHALLENGES [PROTOBUSINESS]
    BUY_ORDER(""),
    SELL_ORDER(""),
    BUY_ORDER_UNIT_50(""),
    BUY_ORDER_64_ITEMS(""),
    BUY_ORDER_500_ITEMS(""),
    SOLD_100_ITEMS(""),
    GAINED_350_COINS(""),
    ORDERED_ALL_ITEMS(""),
    CHALLENGE_COMPLETED(MarketLang.MARKET),
    CHALLENGE_LIST(MarketLang.MARKET),
    CHALLENGE_COMPLETE_FORMAT(""),
    CHALLENGE_INCOMPLETE_FORMAT(""),
    
    // TARIFFS [PROTOBUSINESS]
    TARIFF_LOCAL_INCOMING(NationsLang.NATION),
    TARIFF_LOCAL_OUTGOING(NationsLang.NATION),
    TARIFF_GLOBAL(NationsLang.NATION),
    TARIFF_LOCAL_FORMAT(""),
    TARIFF_GLOBAL_FORMAT(""),
    TARIFF_DOES_NOT_EXIST(NationsLang.NATION),
    TARIFF_RATE_NOT_VALID(NationsLang.NATION),
    NO_TARIFFS_FOUND(NationsLang.NATION),
    INCOMING_TARIFF_SET(NationsLang.NATION),
    INCOMING_TARIFF_REMOVED(NationsLang.NATION),
    OUTGOING_TARIFF_SET(NationsLang.NATION),
    OUTGOING_TARIFF_REMOVED(NationsLang.NATION),
    TARIFF_TYPE_NOT_VALID(NationsLang.NATION),
    TARIFF_ALREADY_EXISTS(NationsLang.NATION),
    TARIFF_FAILED_TO_REMOVE(NationsLang.NATION),
    
    // EMBARGOES
    EMBARGO_ALREADY_EXISTS(NationsLang.NATION),
    EMBARGO_DOES_NOT_EXIST(NationsLang.NATION),
    EMBARGO_NOT_VALID_DURATION(NationsLang.NATION),
    INCOMING_EMBARGO_ADDED(NationsLang.NATION),
    INCOMING_EMBARGO_REMOVED(NationsLang.NATION),
    OUTGOING_EMBARGO_ADDED(NationsLang.NATION),
    OUTGOING_EMBARGO_REMOVED(NationsLang.NATION),
    EMBARGO_FAILED_TO_REMOVE(NationsLang.NATION),
    
    // TRADEBLOCK
    TRADE_BLOCK_LIST_EMPTY(MarketLang.MARKET),
    TRADE_BLOCK_LIST(MarketLang.MARKET),
    TRADE_BLOCK_ADDED(MarketLang.MARKET),
    TRADE_BLOCK_REMOVED(MarketLang.MARKET),
    
    PLAYER_IS_TRADEBLOCKED(MarketLang.MARKET),
    PLAYER_IS_NOT_TRADEBLOCKED(MarketLang.MARKET),
    
    // MARKET STATS
    MARKET_STATS(MarketLang.MARKET),
    MARKET_BUYS(MarketLang.MARKET),
    MARKET_SELLS(MarketLang.MARKET),
    MARKET_TRADES(MarketLang.MARKET),
    MARKET_MONEY_SPENT(MarketLang.MARKET),
    MARKET_MONEY_GAINED(MarketLang.MARKET),
    MARKET_SOLD(MarketLang.MARKET),
    MARKET_BOUGHT(MarketLang.MARKET),
   
    PAGE_INDEX(Lang.PIXLIES),
    PAGE_DOES_NOT_EXIST(Lang.PIXLIES),
    
    // BASIC ECONOMY COMMANDS
    BALANCE(Lang.PIXLIES),
    BALTOP_HEADER(Lang.PIXLIES),
    BALTOP_NATIONS_HEADER(Lang.PIXLIES),
    BALTOP_LAST_UPDATED(Lang.PIXLIES),
    BALTOP_FORMAT(""),
    BALANCE_ADDED_SENDER(Lang.PIXLIES),
    BALANCE_ADDED_TARGET(Lang.PIXLIES),
    PAID_SENDER(Lang.PIXLIES),
    PAID_TARGET(Lang.PIXLIES),
    BALANCE_REMOVED_SENDER(Lang.PIXLIES),
    BALANCE_REMOVED_TARGET(Lang.PIXLIES),
    
    GENERAL_NOT_ENOUGH_MONEY(Lang.PIXLIES),
    MARKET_NOT_ENOUGH_MONEY(MarketLang.MARKET),
    
    GOLD_STANDARD(MarketLang.MARKET),
    CONVERTED_COINS_TO_GOLD(MarketLang.MARKET),
    CONVERTED_GOLD_TO_COINS(MarketLang.MARKET),
    
    // OTHER
    PLAYER_DOES_NOT_EXIST(Lang.PIXLIES),
    PLAYER_HAS_NEVER_JOINED(Lang.PIXLIES);
    
    public static final String MARKET = "§x§1§f§c§a§7§3§lM§x§0§0§b§e§8§d§lA§x§0§0§b§0§a§0§lR§x§0§0§a§1§a§9§lK§x§0§0" +
            "§9§1§a§9§lE§x§1§b§8§1§9§f§lT§8 | §7";
    
    public static final String NAMESPACE = "§x§1§f§c§a§7§3§lM§x§0§0§b§e§8§d§lA§x§0§0§b§0§a§0§lR§x§0§0§a§1§a§9§lK§x§0§0" +
            "§9§1§a§9§lE§x§1§b§8§1§9§f§lT";
    
    private final String prefix;
    private @Getter
    @NotNull Map<String, String> languages = new HashMap<>();
    
    MarketLang(String prefix) {
        this.prefix = CC.format(prefix);
    }
    
    public String get(CommandSender sender) {
        if (sender instanceof Player player) {
            User user = User.get(player.getUniqueId());
            String lang = user.getLang();
            if (languages.containsKey(lang)) {
                return prefix + CC.format(languages.get(user.getLang()));
            } else {
                return get();
            }
        }
        return get();
    }
    
    public String get(CommandSender sender, String... placeholders) {
        String text = get(sender);
        for (String s : placeholders) {
            String[] pSplit = s.split(";");
            text = text.replace(pSplit[0], pSplit[1]);
        }
        return text;
    }
    
    public String get() {
        return prefix + CC.format(languages.get("ENG"));
    }
    
    public String getRaw(String language) {
        if (languages.get(language) == null) return "";
        return languages.get(language);
    }
    
    public void send(CommandSender sender) {
        sender.sendMessage(get(sender));
    }
    
    public void broadcast(String... placeholders) {
        broadcastPermission(null, placeholders);
    }
    
    public void broadcastPermission(String permission, String... placeholders) {
        if (permission == null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                send(player, placeholders);
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(permission)) {
                    send(player, placeholders);
                }
            }
        }
        Bukkit.getConsoleSender().sendMessage(get(null, placeholders));
    }
    
    public void kickPlayer(Player player) {
        player.kickPlayer(get(player));
    }
    
    public void kickPlayer(Player player, String... placeholders) {
        player.kickPlayer(get(player, placeholders));
    }
    
    public void setLanguage(Map<String, String> languages) {
        this.languages = languages;
    }
    
    /**
     * SEND MESSAGE TO PLAYER WITH PLACEHOLDERS
     *
     * @param sender       the command sender to send to
     * @param placeholders get declared by writing TOREPLACE;REPLACED
     */
    public void send(CommandSender sender, String... placeholders) {
        sender.sendMessage(get(sender, placeholders));
    }
    
    public static void load() {
        int loaded = 0;
        File folder = new File(ProtoBusinesss.getInstance().getDataFolder().getAbsolutePath() + "/languages/");
        
        File[] files = folder.listFiles();
        if (files == null) {
            ProtoBusinesss.getInstance().getLogger().warning("Failed to load any languages.");
            return;
        }
        
        for (File file : files) {
            if (!file.getName().endsWith(".yml"))
                continue;
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            String langName = file.getName().replace("LANG_", "").replace(".yml", "").toUpperCase();
            for (MarketLang l : values()) {
                Map<String, String> map = new HashMap<>(l.languages);
                map.put(langName, cfg.getString(l.name()));
                l.setLanguage(map);
            }
            loaded++;
        }
        ProtoBusinesss.getInstance().getLogger().info("[ProtoBusiness] Loaded " + loaded + " languages.");
    }
}
