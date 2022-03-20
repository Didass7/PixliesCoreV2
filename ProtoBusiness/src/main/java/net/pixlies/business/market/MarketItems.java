package net.pixlies.business.market;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.commands.impl.MarketCommand;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.data.UserStats;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.core.utils.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

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
                .addLoreLine("§7Money spent: §6" + stats.getMoneySpent() + " coins")
                .addLoreLine("§7Money gained: §6" + stats.getMoneyGained() + " coins")
                .build();
    }

    public static ItemStack getMarketStats() {
        return new ItemBuilder(new ItemStack(Material.EMERALD))
                .setDisplayName("§aMarket stats")
                .addLoreLine(" ")
                .addLoreLine("§7Buy orders made: §a" + instance.getStats().get("market.buyOrders", 0))
                .addLoreLine("§7Sell orders made: §a" + instance.getStats().get("market.sellOrders", 0))
                .addLoreLine("§7Money spent: §6" + instance.getStats().get("market.moneySpent", 0) + " coins")
                .addLoreLine("§7Money gained: §6" + instance.getStats().get("market.moneyGained", 0) + " coins")
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

    public static ItemStack getOrderItem(Material material, Order order) {
        String name = StringUtils.capitalize(material.name().toLowerCase().replace("_", " "));
        Order.OrderType type = order.getOrderType();

        // TOP INFO

        ItemBuilder builder = new ItemBuilder(new ItemStack(material))
                .setDisplayName((type == Order.OrderType.BUY ? "§a§lBUY" : "§6§lSELL") + "§r§7: §f" + name)
                .addLoreLine("§8Worth " + order.getAmount() * order.getPrice() + " coins") // TODO: taxes and tariffs
                .addLoreLine(" ")
                .addLoreLine("§7Order amount: §a" + order.getAmount() + "§8x");

        // ORDER FILLS

        if (order.getVolume() != order.getAmount()) {
            String percentage = "§a§lFILLED";
            if (order.getVolume() != 0) {
                percentage = "§8(§e" + Math.round((double) order.getVolume() / (double) order.getAmount() * 100) + "%§8)";
            }
            builder.addLoreLine("§7Filled: §a" + (order.getAmount() - order.getVolume()) + "§7/1 " + percentage);
        }

        // PRICE + TAXES

        builder.addLoreLine(" ").addLoreLine("§7Price per unit: §6" + order.getPrice() + " coins");

        if (type == Order.OrderType.BUY) {
            builder.addLoreLine("§7Taxes: §bAMOUNT coins §8(§bPERCENT%§8)"); // TODO: taxes and tariffs
        }

        builder.addLoreLine(" ");

        if (false /* TODO: see if there are goods to claim */) {
            builder.addLoreLine("§eClick to view more options!");
        } else {
            builder.addLoreLine(type == Order.OrderType.BUY ? "§7Vendor(s):" : "§7Buyer(s):");

            // TRADES LIST

            for (Trade trade : order.getTrades()) {
                long secondsTime = (System.currentTimeMillis() - trade.getTimestamp()) / 1000;
                String timestamp = secondsTime + "s";
                if (secondsTime > 60) timestamp = Math.round(secondsTime / 60.0) + "m";

                if (type == Order.OrderType.BUY) {
                    String sellerName = Objects.requireNonNull(Bukkit.getPlayer(trade.getSeller())).getName();
                    builder.addLoreLine("§8- §a" + trade.getAmount() + "§7x " + sellerName + " §8" + timestamp + " ago");
                } else {
                    String takerName = Objects.requireNonNull(Bukkit.getPlayer(trade.getTaker())).getName();
                    builder.addLoreLine("§8- §a" + trade.getAmount() + "§7x " + takerName + " §8" + timestamp + " ago");
                }
            }

            // BOTTOM INFO

            builder.addLoreLine(" ")
                    .addLoreLine("§aYou have §2" + (order.getAmount() - order.getVolume()) + " items§a to claim!")
                    .addLoreLine(" ");

            if (type == Order.OrderType.BUY) {
                builder.addLoreLine("§bRight-click to view more options!");
            }

            builder.addLoreLine(type == Order.OrderType.BUY ? "§eClick to claim items!" : "§eClick to claim coins!");
        }

        return builder.build();
    }

    public static ItemStack getFlipOrderButton(Order order) {
        return new ItemBuilder(new ItemStack(Material.NAME_TAG))
                .setDisplayName("§aFlip order")
                .addLoreLine(" ")
                .addLoreLine("§7Directly create a new sell offer")
                .addLoreLine("§7for §a" + (order.getAmount() - order.getVolume()) + "§8x §7items.")
                .addLoreLine(" ")
                .addLoreLine("§7Current unit price: §6" + order.getPrice() + " coins")
                .addLoreLine(" ")
                .addLoreLine("§eClick to pick a new price!")
                .build();
    }

    public static ItemStack getCancelOrderButton(Order order, boolean cancellable) {
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.RED_TERRACOTTA))
                .setDisplayName("§cCancel order")
                .addLoreLine(" ");
        if (cancellable) {
            if (order.getOrderType() == Order.OrderType.BUY) {
                builder.addLoreLine("§7You will be refunded §6" + (order.getVolume() * order.getPrice()) + " coins§7.");
            } else {
                builder.addLoreLine("§7You will be refunded §a" + order.getVolume() + "§8x §7items.");
            }
            return builder.addLoreLine(" ").addLoreLine("§eClick to cancel!").build();
        } else {
            return builder.addLoreLine("§7Cannot cancel this order because")
                    .addLoreLine("§7there are goods to claim!")
                    .build();
        }
    }

}
