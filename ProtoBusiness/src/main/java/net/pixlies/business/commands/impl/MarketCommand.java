package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.market.MarketItems;
import net.pixlies.business.market.Order;
import net.pixlies.business.market.OrderItem;
import net.pixlies.business.market.Trade;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.data.UserStats;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Handles all Market GUIs
 *
 * @author vPrototype_
 */
@CommandAlias("market|m|nasdaq|nyse|snp500")
@CommandPermission("pixlies.business.market")
public class MarketCommand extends BaseCommand {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);

    @Default
    @Description("Opens the market menu")
    public void onMarket(Player player) {
        openMarketPage(player);
    }

    @Subcommand("open")
    @CommandPermission("pixlies.business.market.gates")
    @Description("Opens the market to the public")
    public void onMarketOpen(Player player) {
        if (marketHandler.isMarketOpen()) Lang.MARKET_WAS_ALREADY_OPEN.send(player);
        else {
            marketHandler.setMarketOpen(true);
            Lang.MARKET_OPEN.broadcast();
        }
    }

    @Subcommand("close")
    @CommandPermission("pixlies.business.market.gates")
    @Description("Closes the market")
    public void onMarketClose(Player player) {
        if (!marketHandler.isMarketOpen()) Lang.MARKET_WAS_ALREADY_CLOSED.send(player);
        else {
            marketHandler.setMarketOpen(false);
            Lang.MARKET_CLOSED.broadcast();
        }
    }

    @Subcommand("reset")
    @CommandPermission("pixlies.business.market.reset")
    @Description("Resets the market statistics")
    public void onMarketReset(Player player, @Optional Player target) {
        if (target == null) {
            instance.getMarketManager().resetBooks();
            Lang.MARKET_STATISTICS_RESET.broadcast("%PLAYER%;" + player.getName());
            player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
        } else {
            if (target.isOnline()) {
                instance.getMarketManager().resetPlayer(target);
                Lang.MARKET_PLAYER_STATISTICS_RESET.send(target, "%PLAYER%;" + target.getName(), "%SENDER%;" + player.getName());
                target.playSound(target.getLocation(), "entity.experience_orb.pickup", 100, 1);
            } else {
                Lang.PLAYER_DOESNT_EXIST.send(player);
            }
        }
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    // ----------------------------------------------------------------------------------------------------

    private StaticPane getMarketPane(Selection selection) {
        StaticPane pane = new StaticPane(2, 0, 7, 5);
        pane.fillWith(new ItemStack(Material.AIR));
        if (!selection.hasSeventhRow()) {
            for (int y = 0; y < 6; y++) pane.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)), 6, y);
        }

        for (OrderItem item : OrderItem.getItemsOfPage(selection.ordinal())) {
            String name = StringUtils.capitalize(item.name().toLowerCase().replace("_", " "));
            ItemBuilder builder = new ItemBuilder(item.getMaterial())
                    .setDisplayName(selection.getColor() + name)
                    .addLoreLine("§7Lowest buy offer: §6" + "$") // TODO lowest buy price
                    .addLoreLine("§7Lowest sell offer: §6" + "$") // TODO lowest sell price
                    .addLoreLine(" ")
                    .addLoreLine("§eClick to buy or sell!");
            // TODO: on item click
            pane.addItem(new GuiItem(builder.build()), item.getPosX(), item.getPosY());
        }

        return pane;
    }

    private ItemBuilder getFills(Order order, ItemBuilder builder) {
        if (order.getVolume() != order.getAmount()) {
            String percentage = "§a§lFILLED";
            if (order.getVolume() != 0) {
                percentage = "§8(§e" + Math.round((double) order.getVolume() / (double) order.getAmount() * 100) + "%§8)";
            }
            builder.addLoreLine("§7Filled: §a" + (order.getAmount() - order.getVolume()) + "§7/1 " + percentage);
        }
        return builder;
    }

    // ----------------------------------------------------------------------------------------------------

    private void openMarketPage(Player player) {

        User user = User.get(player.getUniqueId());
        UserStats stats = user.getStats();
        final Selection[] viewing = { Selection.MINERALS, Selection.MINERALS };

        // CREATE GUI + BACKGROUND

        ChestGui gui = new ChestGui(6, "Market");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // MARKET PANE

        AtomicReference<StaticPane> marketPane = new AtomicReference<>(getMarketPane(viewing[0]));

        // SELECTION PANE

        StaticPane selectionPane = new StaticPane(0, 0, 1, 7);

        for (Selection s : Selection.values()) {

            // ITEM STUFF

            GuiItem item = new GuiItem(s == Selection.MINERALS ? MarketItems.getSelectedSelection(s, s.getName()) :
                    MarketItems.getUnselectedSelection(s, s.getName()));

            // ON ITEM CLICK

            item.setAction(event -> {

                if (s == viewing[0]) return;
                viewing[1] = viewing[0];
                viewing[0] = s;

                // DISABLING THE PREVIOUS BUTTON

                selectionPane.addItem(new GuiItem(MarketItems.getUnselectedSelection(viewing[1], viewing[1].getName())),
                        0, viewing[1].ordinal());

                // ENABLING THE CLICKED BUTTON

                selectionPane.addItem(new GuiItem(MarketItems.getSelectedSelection(viewing[0], viewing[0].getName())),
                        0, viewing[0].ordinal());

                // SHOWING THE NEW MARKET PAGE

                marketPane.set(getMarketPane(viewing[0]));
                gui.update();

            });

            selectionPane.addItem(item, 0, s.ordinal());

        }

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(3, 5, 4, 1);

        // PROFILE STATS

        GuiItem myProfile = new GuiItem(MarketItems.getProfileStats(player));
        bottomPane.addItem(myProfile, 0, 0);

        // MARKET STATS

        GuiItem marketStats = new GuiItem(MarketItems.getMarketStats());
        bottomPane.addItem(marketStats, 1, 0);

        // MY ORDERS

        GuiItem myOrders = new GuiItem(MarketItems.getMyOrdersButton());
        myOrders.setAction(event -> openOrdersPage(player));
        bottomPane.addItem(myOrders, 3, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(marketPane.get());
        gui.addPane(selectionPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    private void openOrdersPage(Player player) {

        player.closeInventory();

        // CREATE GUI + BACKGROUND

        double temp = instance.getMarketManager().getPlayerBuyOrders(player.getUniqueId()).size() / 9D;
        int buys = (int) (temp + (1 - temp % 1));
        temp = instance.getMarketManager().getPlayerSellOrders(player.getUniqueId()).size() / 9F;
        int sells = (int) (temp + (1 - temp % 1));
        int rows = 2 + buys + sells;

        ChestGui gui = new ChestGui(rows, "My orders");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, rows, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // BUY ORDERS PANE

        StaticPane buyOrdersPane = new StaticPane(1, 1, 7, buys);
        buyOrdersPane.fillWith(new ItemStack(Material.AIR));
        int buyIndex = 1;

        for (Map.Entry<Material, Order> entry : instance.getMarketManager().getPlayerBuyOrders(player.getUniqueId()).entrySet()) {

            Material material = entry.getKey();
            Order order = entry.getValue();

            // ITEM STUFF

            String name = StringUtils.capitalize(material.name().toLowerCase().replace("_", " "));
            ItemBuilder builder = new ItemBuilder(new ItemStack(material))
                    .setDisplayName("§a§lBUY§r§7: §f" + name)
                    .addLoreLine("§8Worth " + order.getAmount() * order.getPrice() + "$") // TODO: taxes and tariffs
                    .addLoreLine(" ")
                    .addLoreLine("§7Order amount: §a" + order.getAmount() + "§8x");

            // ORDER FILLED?

            builder = getFills(order, builder);

            builder.addLoreLine(" ")
                    .addLoreLine("§7Price per unit: §6" + order.getPrice() + "$")
                    .addLoreLine("§7Taxes: §bAMOUNT$ §8(§bPERCENT%§8)") // TODO: taxes and tariffs
                    .addLoreLine(" ")
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            if (order.getVolume() == order.getAmount()) {

                builder.addLoreLine("§eClick to view more options!");

            } else {

                builder.addLoreLine("§7Vendor(s):");

                // TIME STUFF + TRADES LIST

                for (Trade trade : order.getTrades()) {

                    String sellerName = Objects.requireNonNull(Bukkit.getPlayer(trade.getSeller())).getName();

                    long secondsTime = (System.currentTimeMillis() - trade.getTimestamp()) / 1000;
                    String timestamp = secondsTime + "s";
                    if (secondsTime > 60) timestamp = Math.round(secondsTime / 60.0) + "m";

                    builder.addLoreLine("§8- §a" + trade.getAmount() + "§7x " + sellerName + " §8" + timestamp + " ago");

                }

                builder.addLoreLine(" ");
                builder.addLoreLine("§aYou have §2" + (order.getAmount() - order.getVolume()) + " items§a to claim!");
                builder.addLoreLine(" ");
                builder.addLoreLine("§bRight-click to view more options!");
                builder.addLoreLine("§eClick to claim items!");

            }

            // ON ITEM CLICK + ADD ITEM TO PANE

            GuiItem item = new GuiItem(builder.build());
            item.setAction(event -> openOrderSettingsPage(player, order));
            buyOrdersPane.addItem(item, buyIndex - (rows * 7), (buyIndex / 7 + (1 - buyIndex / 7 % 1)));
            buyIndex++;

        }

        // SELL ORDERS PANE

        StaticPane sellOrdersPane = new StaticPane(1, buys + 1, 7, sells);
        sellOrdersPane.fillWith(new ItemStack(Material.AIR));

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(4, rows - 1, 1, 1);
        GuiItem goBack = new GuiItem(MarketItems.getBackArrow("Market"));
        goBack.setAction(event -> openMarketPage(player));
        bottomPane.addItem(goBack, 0, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(buyOrdersPane);
        gui.addPane(sellOrdersPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    private void openOrderSettingsPage(Player player, Order order) {

    }

    // ----------------------------------------------------------------------------------------------------

    @AllArgsConstructor
    public enum Selection {
        MINERALS(Material.DIAMOND_PICKAXE, "§b", false),
        FOODSTUFFS_AND_PLANTS(Material.GOLDEN_HOE, "§e", true),
        BLOCKS(Material.IRON_SHOVEL, "§a", true),
        MOB_DROPS(Material.NETHERITE_SWORD, "§c", false),
        MISCELLANEOUS(Material.ARROW, "§6", false),
        STOCKS_AND_BONDS(Material.PAPER, "§d", false);

        @Getter private final Material material;
        @Getter private final String color;
        @Getter private final boolean seventhRow;

        // Lombok not being fun
        public boolean hasSeventhRow() {
            return seventhRow;
        }

        public String getName() {
            return StringUtils.capitalize(name().toLowerCase().replace("_", " "));
        }
    }

}
