package net.pixlies.business.items;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.guis.MarketInitialGUI;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.profiles.MarketProfile;
import net.pixlies.business.util.MarketUtil;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.core.utils.PlayerUtils;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Ու տես որդիս, ուր էլ լինես,
 * Այս լուսնի տակ ուր էլ գնաս,
 * Թե մորդ անգամ մտքից հանես,
 * Քո Մայր լեզուն չմոռանա՛ս...
 *
 * @author սիլվա կապուտիկեան
 */
public final class MarketGUIItems {
    public static final ProtoBusiness instance = ProtoBusiness.getInstance();
    
    public static ItemStack getBackArrow(String text) {
        return new ItemBuilder(new ItemStack(Material.ARROW))
                .setDisplayName("§aGo back")
                .addLoreLine("§8[" + text + "§8]")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }
    
    public static ItemStack getEmptyItem() {
        return new ItemBuilder(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                .setDisplayName("")
                .build();
    }
    
    // --------------------------------------------------------------------------------------------
    
    public static ItemStack getProfileStats(Player player) {
        MarketProfile profile = MarketProfile.get(player.getUniqueId());
        
        return new ItemBuilder(PlayerUtils.getSkull(player.getUniqueId()))
                .setDisplayName("§6" + player.getName() + "'s Statistics")
                .addLoreLine(" ")
                .addLoreLine("§7Buy orders made: §b" + profile.getBuyOrdersMade())
                .addLoreLine("§7Sell orders made: §b" + profile.getSellOrdersMade())
                .addLoreLine("§7Trades made: §a" + profile.getTradesMade())
                .addLoreLine(" ")
                .addLoreLine("§7Money spent: §6" + profile.getMoneySpent() + " coins")
                .addLoreLine("§7Money gained: §6" + profile.getMoneyGained() + " coins")
                .addLoreLine("§7Items sold: §d" + profile.getMoneySpent() + " items")
                .addLoreLine("§7Items bought: §d" + profile.getMoneyGained() + " items")
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
                .setDisplayName("§aMarket Statistics")
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
        List<Order> orders = MarketUtil.getPlayerBuyOrders(player.getUniqueId());
        orders.addAll(MarketUtil.getPlayerSellOrders(player.getUniqueId()));
        
        // Check if there are any goods to claim
        boolean goods = false;
        for (Order order : orders) {
            if (!order.isCancellable()) {
                goods = true;
                break;
            }
        }
        
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.BOOK))
                .setDisplayName("§bPlaced Orders")
                .addLoreLine(" ");
        
        if (goods) {
            builder.addLoreLine("§aYou have items to claim!");
        }
        
        return builder.addLoreLine("§eClick to view your orders!").build();
    }
    
    // --------------------------------------------------------------------------------------------
    
    public static ItemStack getSelectedSelection(MarketInitialGUI.Selection s, String name) {
        return new ItemBuilder(new ItemStack(s.getMaterial()))
                .setDisplayName(s.getColor() + name)
                .addLoreLine(" ")
                .removeLoreLine("§eClick to view this tab!")
                .addLoreLine("§aYou are viewing this tab!")
                .setGlow(true)
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }
    
    public static ItemStack getUnselectedSelection(MarketInitialGUI.Selection s, String name) {
        return new ItemBuilder(new ItemStack(s.getMaterial()))
                .setDisplayName(s.getColor() + name)
                .addLoreLine(" ")
                .removeLoreLine("§aYou are viewing this tab!")
                .addLoreLine("§eClick to view this tab!")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }
    
    // --------------------------------------------------------------------------------------------
    
    public static ItemStack getOrderItem(Material material, Order order) {
        String name = OrderBook.get(order.getBookItem()).getItem().name();
        Order.Type type = order.getType();
        String maxOrMin = type == Order.Type.BUY ? "max." : "min.";
    
        // Top info
        ItemBuilder builder = new ItemBuilder(new ItemStack(material))
                .setDisplayName((type == Order.Type.BUY ? "§a§lBUY" : "§6§lSELL") + "§r§7: §b" +
                                order.getAmount() + "§8x §f" + name)
                .addLoreLine(" ")
                .addLoreLine("§7Price per unit: §f" + maxOrMin + " §6" + order.getPrice() + " coins");
        
        // Percentage of the order that is filled
        if (order.getVolume() != order.getAmount()) {
            int percentage = (int) Math.round((double) order.getVolume() / (double) order.getAmount() * 100);
            String strPercentage = "§a§lFILLED";
            if (order.getVolume() != 0) {
                strPercentage = "§8(§e" + percentage + "%§8)";
            }
            String amount = "§a" + (order.getAmount() - order.getVolume()) + "§7/" + order.getAmount();
            builder.addLoreLine("§7Filled: " + amount + " " + strPercentage);
        }
        
        // Price with sales taxes
        Nation nation = NationProfile.get(order.getPlayerUUID()).getNation();
        double tax = nation.getTaxRate();
        double price = (order.getPrice() * order.getAmount() * (1 + tax));
        builder.addLoreLine("§7§lTotal price: §f" + maxOrMin + " §6" + price + " coins")
                .addLoreLine(" ");
        
        // If there are no trades, build the ItemStack and return
        if (order.getTrades().size() == 0) {
            return builder.addLoreLine("§eClick to view more options!").build();
        }
    
        // Trades list
        builder.addLoreLine(type == Order.Type.BUY ? "§7Vendor(s):" : "§7Buyer(s):");
        order.getTrades().forEach(trade -> builder.addLoreLine(trade.toString()));
        
        // Refunds
        builder.addLoreLine(" ")
                .addLoreLine("§8Refunded: §6" + order.getTotalRefunds() + " coins");
        
        // Cancel order
        if (order.isCancellable()) {
            return builder.addLoreLine(" ")
                    .addLoreLine("§cClick to cancel!")
                    .build();
        }
        
        // Claiming goods or coins
        if (type == Order.Type.BUY) {
            builder.addLoreLine(" ")
                    .addLoreLine("§aYou have §2" + order.getItemsToClaim() + " items §ato claim!");
        } else {
            double coinsToClaim = order.getRefundableCoins() + order.getCoinsToClaim();
            builder.addLoreLine(" ")
                    .addLoreLine("§eYou have §6" + coinsToClaim + " coins §eto claim!");
        }
        
        return builder.addLoreLine(" ")
                .addLoreLine("§eClick to claim!")
                .build();
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
        OrderItem item = OrderBook.get(order.getBookItem()).getItem();
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
        OrderBook book = OrderBook.get(item);
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
        OrderBook book = OrderBook.get(item);
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.GOLD_INGOT))
                .setDisplayName("§6Sell Order")
                .addLoreLine("§7Best price per unit: §6" + book.getHighestSellPrice(playerUUID))
                .addLoreLine("§7Inventory: §a" + num + " items")
                .addLoreLine("§7Best total price: §d" + (book.getHighestSellPrice(playerUUID) * num))
                .addLoreLine(" ");
        for (String s : book.getRecentOrders(Order.Type.SELL, item, playerUUID)) builder.addLoreLine(s);
        return builder.addLoreLine(" ")
                .addLoreLine("§8This is a limit order.")
                .addLoreLine("§eClick to create!")
                .build();
    }
    
    public static ItemStack getBestPriceButton(UUID playerUUID, OrderItem item, Order.Type type, int amount) {
        OrderBook book = OrderBook.get(item);
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
                .addLoreLine("§eClick to set!")
                .build();
    }
    
    public static ItemStack getChangedPriceButton(UUID playerUUID, OrderItem item, Order.Type type, int amount) {
        OrderBook book = OrderBook.get(item);
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
