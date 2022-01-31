package net.pixlies.core.economy;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.utils.Palette;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Wallet {

    private String name;
    private String sign;
    private double balance;
    private List<String> history;
    private boolean prefix;
    private Palette palette;

    public void deposit(double amount, String reason) {
        this.balance += amount;
        history.add("+;" + amount + ";" + reason + ";" + System.currentTimeMillis());
    }

    public boolean withdraw(double amount, String reason) {
        if (this.balance - amount < 0) return false;
        this.balance -= amount;
        this.history.add("-;" + amount + ";" + reason + ";" + System.currentTimeMillis());
        return true;
    }

    public String format(double amount) {
        StringBuilder builder = new StringBuilder();
        if (prefix) {
            builder.append(palette.getEffect());
            builder.append(palette.getAccent());
            builder.append(sign);
            builder.append(ChatColor.RESET);
        }
        builder.append(palette.getPrimary());
        builder.append(amount);
        if (!prefix) {
            builder.append(palette.getEffect());
            builder.append(palette.getAccent());
            builder.append(sign);
            builder.append(ChatColor.RESET);
        }
        return builder.toString();
    }

    public String formatBalance() {
        return format(balance);
    }

    public Map<String, Object> mapForMongo() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("sign", sign);
        map.put("balance", balance);
        map.put("history", history);
        map.put("prefix", prefix);
        map.put("palette", palette.getEffect().getChar() + ";" +  palette.getPrimary().getChar() + ";" + palette.getAccent().getChar());
        return map;
    }

    public Wallet(Map<String, Object> mapped) {
        this.name = (String) mapped.get("name");
        this.sign = (String) mapped.get("sign");
        this.balance = (double) mapped.get("balance");
        this.history = (List<String>) mapped.get("history");
        this.prefix = (boolean) mapped.get("prefix");
        String[] paletteList = ((String) mapped.get("palette")).split(";");
        this.palette = new Palette(ChatColor.getByChar(paletteList[0]), ChatColor.getByChar(paletteList[1]), ChatColor.getByChar(paletteList[2]));
    }

    public static Map<String, Wallet> getFromMongo(Map<String, Map<String, Object>> map) {
        Map<String, Wallet> walletMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet())
            walletMap.put(entry.getKey(), new Wallet(entry.getValue()));
        return walletMap;
    }

    public static Map<String, Map<String, Object>> mapAllForMongo(Map<String, Wallet> walletMap) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        for (Map.Entry<String, Wallet> entry : walletMap.entrySet())
            map.put(entry.getKey(), entry.getValue().mapForMongo());
        return map;
    }

    public static Map<String, Wallet> getDefaultWallets() {
        Map<String, Wallet> wallets = new HashMap<>();
        wallets.put("serverCurrency", new Wallet(
                "Dollar",
                "$",
                0,
                new ArrayList<>(),
                true,
                Palette.GREEN_THICK
        ));
        return wallets;
    }

}
