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
        history.add("+;" + amount + ";" + reason.replace(";", ":") + ";" + System.currentTimeMillis());
    }

    public boolean withdraw(double amount, String reason) {
        if (this.balance - amount < 0) return false;
        this.balance -= amount;
        this.history.add("-;" + amount + ";" + reason.replace(";", ":") + ";" + System.currentTimeMillis());
        return true;
    }

    /**
     * Check if the player is able to withdraw a certain amount of money
     * @param amount The amount of cash to check
     * @return True of the player can withdraw; False if the player cannot withdraw.
     */
    public boolean canWithdraw(double amount) {
        return !(this.balance - amount < 0);
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

    public String getFormattedBalance() {
        return format(balance);
    }

    public static Map<String, Wallet> getDefaults() {
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
