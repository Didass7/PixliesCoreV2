package net.pixlies.business.market;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.commands.impl.MarketCommand;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.orders.Trade;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.core.utils.PlayerUtils;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

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
        
        return new ItemBuilder(PlayerUtils.getSkull(player.getUniqueId()))
                .setDisplayName("§6" + player.getName() + "'s stats")
                .addLoreLine(" ")
                .addLoreLine("§7Buy orders made: §b" + user.getBuyOrdersMade())
                .addLoreLine("§7Sell orders made: §b" + user.getSellOrdersMade())
                .addLoreLine("§7Trades made: §a" + user.getTradesMade())
                .addLoreLine(" ")
                .addLoreLine("§7Money spent: §6" + user.getMoneySpent() + " coins")
                .addLoreLine("§7Money gained: §6" + user.getMoneyGained() + " coins")
                .addLoreLine("§7Items sold: §d" + user.getMoneySpent() + " items")
                .addLoreLine("§7Items bought: §d" + user.getMoneyGained() + " items")
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
        Order.Type type = order.getType();
        
        // TOP INFO
        
        ItemBuilder builder = new ItemBuilder(new ItemStack(material))
                .setDisplayName((type == Order.Type.BUY ? "§a§lBUY" : "§6§lSELL") + "§r§7: §f" + name)
                .addLoreLine("§8>> §b" + order.getAmount() + "§8x §b" + name)
                .addLoreLine(" ")
                .addLoreLine("§7Price per unit: §6" + order.getPrice() + " coins");
        
        
        // ORDER FILLS
        
        if (order.getVolume() != order.getAmount()) {
            String percentage = "§a§lFILLED";
            if (order.getVolume() != 0) {
                percentage =
                        "§8(§e" + Math.round((double) order.getVolume() / (double) order.getAmount() * 100) + "%§8)";
            }
            String amount = "§a" + (order.getAmount() - order.getVolume()) + "§7/" + order.getAmount() + " ";
            builder.addLoreLine("§7Filled: " + amount + percentage);
        }
        
        // PRICE
        
        Nation nation = NationProfile.get(order.getPlayerUUID()).getNation();
        double tax = nation.getTaxRate();
        
        builder.addLoreLine("§7§lTotal price: §6" + (order.getPrice() * order.getAmount() * (1 + tax)) + " coins")
                .addLoreLine(" ");
        
        if (order.getVolume() == order.getAmount()) {
            builder.addLoreLine("§eClick to view more options!");
        } else {
            builder.addLoreLine(type == Order.Type.BUY ? "§7Vendor(s):" : "§7Buyer(s):");
            
            // TRADES LIST
            
            for (Trade trade : order.getTrades()) {
                builder.addLoreLine(trade.toString(order.getPrice()));
            }
            
            // BOTTOM INFO
            
            if (order.isCancellable()) {
                builder.addLoreLine("§cClick to cancel!");
            } else {
                builder.addLoreLine(" ")
                        .addLoreLine("§aYou have §2" + (order.getAmount() - order.getVolume()) + " items§a to" +
                                " claim!")
                        .addLoreLine(" ")
                        .addLoreLine(type == Order.Type.BUY ? "§eClick to claim items!" : "§eClick to claim " +
                                "coins!");
            }
        }
        
        return builder.build();
    }
    
    public static ItemStack getCancelOrderButton(Order order) {
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.RED_TERRACOTTA))
                .setDisplayName("§cCancel order")
                .addLoreLine(" ");
        if (order.getType() == Order.Type.BUY) {
            builder.addLoreLine("§7You will be refunded §6" + (order.getVolume() * order.getPrice()) + " coins" +
                    "§7.");
        } else {
            builder.addLoreLine("§7You will be refunded §a" + order.getVolume() + "§8x §7items.");
        }
        return builder.addLoreLine(" ").addLoreLine("§eClick to cancel!").build();
    }
    
    public static ItemStack getConfirmOrderButton(Order order, double tax) {
        OrderItem item = instance.getMarketManager().getBooks().get(order.getBookId()).getItem();
        return new ItemBuilder(new ItemStack(item.getMaterial()))
                .setDisplayName(order.getType() == Order.Type.BUY ?
                        "§aConfirm order §8(§a§lBUY§r§8)" :
                        "§aConfirm order §8(§6§lSELL§r§8)")
                .addLoreLine("§8>> §b" + order.getAmount() + "§8x §b" + item.getName())
                .addLoreLine(" ")
                .addLoreLine("§7Price per unit: §6" + order.getPrice() + " coins")
                .addLoreLine("§7Tax: §c" + (tax * 100) + "%")
                .addLoreLine("§7§lTotal price: §6" + (order.getPrice() * order.getAmount() * (1 + tax)) + " coins")
                .addLoreLine(" ")
                .addLoreLine("§8ID: " + order.getOrderId())
                .addLoreLine("§eClick to confirm order!")
                .build();
    }
    
    public static ItemStack getBuyButton(UUID playerUUID, OrderItem item) {
        OrderBook book = instance.getMarketManager().getBook(item);
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.EMERALD))
                .setDisplayName("§aBuy Order")
                .addLoreLine("§7Best price per unit: §6" + book.getLowestBuyPrice(playerUUID))
                .addLoreLine(" ");
        for (String s : book.getRecentOrders(Order.Type.BUY, item, playerUUID)) builder.addLoreLine(s);
        return builder.addLoreLine(" ")
                .addLoreLine("§8This is a limit order.")
                .addLoreLine("§eClick to create!")
                .build();
    }
    
    public static ItemStack getSellButton(UUID playerUUID, OrderItem item, int num) {
        OrderBook book = instance.getMarketManager().getBook(item);
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.GOLD_INGOT))
                .setDisplayName("§6Sell Order")
                .addLoreLine("§7Best price per unit: §6" + book.getHighestSellPrice(playerUUID))
                .addLoreLine("§7Inventory: §a" + num + " items")
                .addLoreLine("§7Best total price: §d" + (book.getHighestSellPrice(playerUUID) * num))
                .addLoreLine(" ");
        for (String s : book.getRecentOrders(Order.Type.SELL, item, playerUUID)) builder.addLoreLine(s);
        return builder.addLoreLine(" ")
                .addLoreLine("§8Tariffs already applied.")
                .addLoreLine("§8This is a limit order.")
                .addLoreLine("§eClick to create!")
                .build();
    }
    
    public static ItemStack getBestPriceButton(UUID playerUUID, OrderItem item, Order.Type type, int amount) {
        OrderBook book = instance.getMarketManager().getBook(item);
        return new ItemBuilder(new ItemStack(item.getMaterial()))
                .setDisplayName("§eBest current price")
                .addLoreLine(type == Order.Type.BUY ?
                        "§7Price: §6" + book.getLowestBuyPrice(playerUUID) + " coins" :
                        "§7Price: §6" + book.getHighestSellPrice(playerUUID) + " coins")
                .addLoreLine(" ")
                .addLoreLine("§7Selling: §a" + amount + "§8x")
                .addLoreLine(type == Order.Type.BUY ?
                        "§7§lTotal price: §6" + (book.getLowestBuyPrice(playerUUID) * amount) + " coins" :
                        "§7§lTotal price: §6" + (book.getHighestSellPrice(playerUUID) * amount) + " coins")
                .addLoreLine(" ")
                .addLoreLine("§8Tariffs already applied.")
                .addLoreLine("§eClick to set!")
                .build();
    }
    
    public static ItemStack getChangedPriceButton(UUID playerUUID, OrderItem item, Order.Type type, int amount) {
        OrderBook book = instance.getMarketManager().getBook(item);
        return new ItemBuilder(new ItemStack(Material.GOLD_NUGGET))
                .setDisplayName(type == Order.Type.BUY ?
                        "§eBest current price +0.1" :
                        "§eBest current price -0.1")
                .addLoreLine(type == Order.Type.BUY ?
                        "§7Price: §6" + (book.getLowestBuyPrice(playerUUID) + 0.1) + " coins" :
                        "§7Price: §6" + (book.getHighestSellPrice(playerUUID) - 0.1) + " coins")
                .addLoreLine(" ")
                .addLoreLine(type == Order.Type.BUY ?
                        "§7§lTotal price: §6" + ((book.getLowestBuyPrice(playerUUID) + 0.1) * amount) + " coins" :
                        "§7§lTotal price: §6" + ((book.getHighestSellPrice(playerUUID) - 0.1) * amount) + " coins")
                .addLoreLine(" ")
                .addLoreLine("§8Tariffs already applied.")
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
