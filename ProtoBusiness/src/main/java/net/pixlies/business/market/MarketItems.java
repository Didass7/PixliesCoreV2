package net.pixlies.business.market;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.commands.impl.MarketCommand;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.orders.Trade;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.data.UserStats;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
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
                .addLoreLine("§7Trades made: §a" + stats.getTradesMade())
                .addLoreLine(" ")
                .addLoreLine("§7Money spent: §6" + stats.getMoneySpent() + " coins")
                .addLoreLine("§7Money gained: §6" + stats.getMoneyGained() + " coins")
                .addLoreLine("§7Items sold: §d" + stats.getMoneySpent() + " items")
                .addLoreLine("§7Items bought: §d" + stats.getMoneyGained() + " items")
                .addLoreLine(" ")
                /*
                .addLoreLine("§7Item most sold: §3" + a)
                .addLoreLine("§7Item most bought: §3" + a)
                .addLoreLine("§7Coins most spent on: §c" + a)
                .addLoreLine("§7Coins most gained on: §c" + a)
                 */
                .build();
    }

    public static ItemStack getMarketStats() {
        return new ItemBuilder(new ItemStack(Material.EMERALD))
                .setDisplayName("§aMarket stats")
                .addLoreLine(" ")
                .addLoreLine("§7Buy orders made: §b" + instance.getStats().get("market.buyOrders", 0))
                .addLoreLine("§7Sell orders made: §b" + instance.getStats().get("market.sellOrders", 0))
                .addLoreLine("§7Trades made: §a" + instance.getStats().get("market.trades", 0))
                .addLoreLine(" ")
                .addLoreLine("§7Money traded: §6" + instance.getStats().get("market.moneyTraded", 0) + " coins")
                .addLoreLine("§7Items traded: §d" + instance.getStats().get("market.itemsTraded", 0) + " items")
                .addLoreLine(" ")
                /*
                .addLoreLine("§7Item most sold: §3" + a)
                .addLoreLine("§7Item most bought: §3" + a)
                .addLoreLine("§7Coins most spent on: §c" + a)
                .addLoreLine("§7Coins most gained on: §c" + a)
                .addLoreLine(" ")
                .addLoreLine("§7Most buy orders: §b" + a)
                .addLoreLine("§7Most sell orders: §b" + a)
                .addLoreLine("§7Most orders: §a" + a)
                */
                .build();
    }

    public static ItemStack getMyOrdersButton(Player player) {
        List<Order> orders = instance.getMarketManager().getPlayerBuyOrders(player.getUniqueId());
        orders.addAll(instance.getMarketManager().getPlayerSellOrders(player.getUniqueId()));

        boolean goods = false;
        for (Order order : orders) {
            if (!order.isCancellable()) {
                goods = true;
                break;
            }
        }

        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.BOOK))
                .setDisplayName("§bMy orders")
                .addLoreLine(" ");
        if (goods) {
            return builder.addLoreLine("§eClick to view your orders!").build();
        } else {
            return builder.addLoreLine("§aYou have items to claim!").addLoreLine("§eClick to view your orders!").build();
        }
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
        String name = instance.getMarketManager().getBooks().get(order.getBookId()).getItem().getName();
        Order.OrderType type = order.getOrderType();

        // TOP INFO

        ItemBuilder builder = new ItemBuilder(new ItemStack(material))
                .setDisplayName((type == Order.OrderType.BUY ? "§a§lBUY" : "§6§lSELL") + "§r§7: §f" + name)
                .addLoreLine("§8Worth " + order.getAmount() * order.getPrice() + " coins") // TODO: taxes and tariffs
                .addLoreLine(" ")
                .addLoreLine("§7Order amount: §a" + order.getAmount() + "§8x");

        // ORDER FILLS + AMOUNT

        if (order.getVolume() != order.getAmount()) {
            String percentage = "§a§lFILLED";
            if (order.getVolume() != 0) {
                percentage = "§8(§e" + Math.round((double) order.getVolume() / (double) order.getAmount() * 100) + "%§8)";
            }
            String amount = "§a" + (order.getAmount() - order.getVolume()) + "§7/" + order.getAmount() + " ";
            builder.addLoreLine("§7Filled: " + amount + percentage);
        }

        // PRICE

        builder.addLoreLine(" ").addLoreLine("§7Price per unit: §6" + order.getPrice() + " coins").addLoreLine(" ");

        if (order.getVolume() == order.getAmount()) {
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
                    builder.addLoreLine("§8- §a" + trade.getAmount() + "§7x §f" + sellerName + " §8" + timestamp + " ago");
                } else {
                    String takerName = Objects.requireNonNull(Bukkit.getPlayer(trade.getTaker())).getName();
                    builder.addLoreLine("§8- §a" + trade.getAmount() + "§7x " + takerName + " §8" + timestamp + " ago");
                }
            }

            // BOTTOM INFO

            if (order.isCancellable()) {
                builder.addLoreLine("§cClick to cancel!");
            } else {
                builder.addLoreLine(" ")
                        .addLoreLine("§aYou have §2" + (order.getAmount() - order.getVolume()) + " items§a to claim!")
                        .addLoreLine(" ")
                        .addLoreLine(type == Order.OrderType.BUY ? "§eClick to claim items!" : "§eClick to claim coins!");
            }
        }

        return builder.build();
    }

    public static ItemStack getCancelOrderButton(Order order) {
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.RED_TERRACOTTA))
                .setDisplayName("§cCancel order")
                .addLoreLine(" ");
        if (order.getOrderType() == Order.OrderType.BUY) {
            builder.addLoreLine("§7You will be refunded §6" + (order.getVolume() * order.getPrice()) + " coins§7.");
        } else {
            builder.addLoreLine("§7You will be refunded §a" + order.getVolume() + "§8x §7items.");
        }
        return builder.addLoreLine(" ").addLoreLine("§eClick to cancel!").build();
    }

    public static ItemStack getConfirmOrderButton(Order order, double tax) {
        OrderItem item = instance.getMarketManager().getBooks().get(order.getBookId()).getItem();
        return new ItemBuilder(new ItemStack(item.getMaterial()))
                .setDisplayName(order.getOrderType() == Order.OrderType.BUY ?
                        "§aConfirm order §8(§a§lBUY§r§8)" :
                        "§aConfirm order §8(§6§lSELL§r§8)")
                .addLoreLine("§f" + item.getName())
                .addLoreLine(" ")
                .addLoreLine("§7Amount: §b" + order.getAmount() + "§8x §b" + item.getName())
                .addLoreLine("§7Price per unit: §6" + order.getPrice() + " coins")
                .addLoreLine("§7Limit order: §d" + order.isLimitOrder())
                .addLoreLine(" ")
                .addLoreLine("§7Tax: §c" + (tax * 100) + "%")
                .addLoreLine("§7§lTotal price: §6" + (order.getPrice() * order.getAmount() * (1 + tax)) + " coins")
                .addLoreLine(" ")
                .addLoreLine("§8ID: " + order.getOrderId())
                .addLoreLine("§eClick to confirm order!")
                .build();
        // TODO: tariffs and discounts
    }

    public static ItemStack getMarketBuyButton(OrderItem item) {
        OrderBook book = instance.getMarketManager().getBookFromItem(item);
        return new ItemBuilder(new ItemStack(Material.EMERALD))
                .setDisplayName("§a§lMarket buy order")
                .addLoreLine(" ")
                .addLoreLine("§7A market order is an")
                .addLoreLine("§7order at a fixed price.")
                .addLoreLine(" ")
                .addLoreLine("§7Best price: §6" + book.getLowestBuyPrice())
                .addLoreLine(" ")
                .addLoreLine("§eClick to create!")
                .build();
    }

    public static ItemStack getLimitBuyButton(OrderItem item) {
        OrderBook book = instance.getMarketManager().getBookFromItem(item);
        return new ItemBuilder(new ItemStack(Material.EMERALD_BLOCK))
                .setDisplayName("§aLIMIT buy order")
                .addLoreLine(" ")
                .addLoreLine("§7A limit buy order is an")
                .addLoreLine("§7order with a maximum buy")
                .addLoreLine("§7price.")
                .addLoreLine(" ")
                .addLoreLine("§7Best price: §6" + book.getLowestBuyPrice())
                .addLoreLine(" ")
                .addLoreLine("§eClick to create!")
                .build();
    }

    public static ItemStack getMarketSellButton(OrderItem item, int num) {
        OrderBook book = instance.getMarketManager().getBookFromItem(item);
        return new ItemBuilder(new ItemStack(Material.GOLD_INGOT))
                .setDisplayName("§6§lMarket sell order")
                .addLoreLine(" ")
                .addLoreLine("§7A market order is an")
                .addLoreLine("§7order at a fixed price.")
                .addLoreLine(" ")
                .addLoreLine("§7Best price: §6" + book.getHighestSellPrice())
                .addLoreLine("§7Inventory: §a" + num + " items")
                .addLoreLine(" ")
                .addLoreLine("§eClick to create!")
                .build();
    }

    public static ItemStack getLimitSellButton(OrderItem item, int num) {
        OrderBook book = instance.getMarketManager().getBookFromItem(item);
        return new ItemBuilder(new ItemStack(Material.GOLD_BLOCK))
                .setDisplayName("§6LIMIT sell order")
                .addLoreLine(" ")
                .addLoreLine("§7A limit buy order is an")
                .addLoreLine("§7order with a minimum sell")
                .addLoreLine("§7price.")
                .addLoreLine(" ")
                .addLoreLine("§7Best price: §6" + book.getHighestSellPrice())
                .addLoreLine("§7Inventory: §a" + num + " items")
                .addLoreLine(" ")
                .addLoreLine("§eClick to create!")
                .build();
    }

    public static ItemStack getBestPriceButton(OrderItem item, Order.OrderType type, int amount) {
        OrderBook book = instance.getMarketManager().getBookFromItem(item);
        return new ItemBuilder(new ItemStack(item.getMaterial()))
                .setDisplayName("§eBest current price")
                .addLoreLine(type == Order.OrderType.BUY ?
                        "§7Price: §6" + book.getLowestBuyPrice() + " coins" :
                        "§7Price: §6" + book.getHighestSellPrice() + " coins" )
                .addLoreLine(" ")
                .addLoreLine("§7Selling: §a" + amount + "§8x")
                .addLoreLine(type == Order.OrderType.BUY ?
                        "§7§lTotal price: §6" + (book.getLowestBuyPrice() * amount) + " coins" :
                        "§7§lTotal price: §6" + (book.getHighestSellPrice() * amount) + " coins" )
                .addLoreLine(" ")
                .addLoreLine("§eClick to set!")
                .build();
    }

    public static ItemStack getChangedPriceButton(OrderItem item, Order.OrderType type, int amount) {
        OrderBook book = instance.getMarketManager().getBookFromItem(item);
        return new ItemBuilder(new ItemStack(Material.GOLD_NUGGET))
                .setDisplayName(type == Order.OrderType.BUY ?
                        "§eBest current price +0.1" :
                        "§eBest current price -0.1")
                .addLoreLine(type == Order.OrderType.BUY ?
                        "§7Price: §6" + (book.getLowestBuyPrice() + 0.1) + " coins" :
                        "§7Price: §6" + (book.getHighestSellPrice() - 0.1) + " coins" )
                .addLoreLine(" ")
                .addLoreLine(type == Order.OrderType.BUY ?
                        "§7§lTotal price: §6" + ((book.getLowestBuyPrice() + 0.1) * amount) + " coins" :
                        "§7§lTotal price: §6" + ((book.getHighestSellPrice() - 0.1 ) * amount) + " coins" )
                .addLoreLine(" ")
                .addLoreLine("§eClick to set!")
                .build();
    }

    public static ItemStack getCustomPriceButton() {
        return new ItemBuilder(new ItemStack(Material.NAME_TAG))
                .setDisplayName("§eCustom price")
                .addLoreLine("§7Set a custom price for")
                .addLoreLine("§7your needs.")
                .addLoreLine(" ")
                .addLoreLine("§eClick to set!")
                .build();
    }


}
