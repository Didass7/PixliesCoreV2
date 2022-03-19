package net.pixlies.business.market;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.commands.impl.MarketCommand;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.data.UserStats;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public final class MarketItems {

    public static final ProtoBusiness instance = ProtoBusiness.getInstance();

    public static ItemStack getBackArrow(String text) {
        return new ItemBuilder(new ItemStack(Material.ARROW))
                .setDisplayName("§aGo back")
                .addLoreLine("§7[" + text + "]")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }

    // MARKET PAGE BOTTOM PANE

    public static ItemStack getProfileStats(Player player) {
        User user = User.get(player.getUniqueId());
        UserStats stats = user.getStats();

        return new ItemBuilder(PlayerUtils.getSkull(player.getUniqueId()))
                .setDisplayName("§6" + player.getName() + "'s stats")
                .addLoreLine(" ")
                .addLoreLine("§7Buy orders made: §b" + stats.getBuyOrdersMade())
                .addLoreLine("§7Sell orders made: §b" + stats.getSellOrdersMade())
                .addLoreLine("§7Money spent: §6" + stats.getMoneySpent())
                .addLoreLine("§7Money gained: §6" + stats.getMoneyGained())
                .build();
    }

    public static ItemStack getMarketStats() {
        return new ItemBuilder(new ItemStack(Material.EMERALD))
                .setDisplayName("§aMarket stats")
                .addLoreLine(" ")
                .addLoreLine("§7Buy orders made: §a" + instance.getStats().get("market.buyOrders", 0))
                .addLoreLine("§7Sell orders made: §a" + instance.getStats().get("market.sellOrders", 0))
                .addLoreLine("§7Money spent: §6" + instance.getStats().get("market.moneySpent", 0))
                .addLoreLine("§7Money gained: §6" + instance.getStats().get("market.moneyGained", 0))
                .build();
    }

    public static ItemStack getMyOrdersButton() {
        // TODO: show if I have items or coins to collect
        return new ItemBuilder(new ItemStack(Material.BOOK))
                .setDisplayName("§bMy orders")
                .addLoreLine(" ")
                .addLoreLine("§eClick to view your orders!")
                .build();
    }

    // MARKET PAGE SELECTION PANE

    public static ItemStack getSelectedSelection(MarketCommand.Selection s, String name) {
        return new ItemBuilder(new ItemStack(s.getMaterial()))
                .setDisplayName(s.getColor() + name)
                .addLoreLine(" ")
                .removeLoreLine("§eClick to view this tab!")
                .addLoreLine("§aYou are viewing this tab!")
                .setGlow(true)
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }

    public static ItemStack getUnselectedSelection(MarketCommand.Selection s, String name) {
        return new ItemBuilder(new ItemStack(s.getMaterial()))
                .setDisplayName(s.getColor() + name)
                .addLoreLine(" ")
                .removeLoreLine("§aYou are viewing this tab!")
                .addLoreLine("§eClick to view this tab!")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }

    // MY ORDERS PAGE

}
