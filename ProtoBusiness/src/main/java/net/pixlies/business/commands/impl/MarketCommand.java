package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.market.MarketItems;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.data.UserStats;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
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
            OrderBook book = item.getBook();
            assert book != null;
            ItemBuilder builder = new ItemBuilder(item.getMaterial())
                    .setDisplayName(selection.getColor() + item.getName())
                    .addLoreLine("§7Lowest buy offer: §6" + book.getLowestBuyPrice() + " coins")
                    .addLoreLine("§7Highest sell offer: §6" + book.getHighestSellPrice() + " coins")
                    .addLoreLine(" ")
                    .addLoreLine("§eClick to buy or sell!");
            // TODO: on item click
            pane.addItem(new GuiItem(builder.build()), item.getPosX(), item.getPosY());
        }

        return pane;
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

        GuiItem myProfile = new GuiItem(MarketItems.getProfileStats(player));
        bottomPane.addItem(myProfile, 0, 0);

        GuiItem marketStats = new GuiItem(MarketItems.getMarketStats());
        bottomPane.addItem(marketStats, 1, 0);

        GuiItem myOrders = new GuiItem(MarketItems.getMyOrdersButton(player));
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

        List<Order> buys = instance.getMarketManager().getPlayerBuyOrders(player.getUniqueId());
        List<Order> sells = instance.getMarketManager().getPlayerSellOrders(player.getUniqueId());
        int rows = (int) Math.round(((buys.size() + sells.size()) / 7.0) + 0.5);

        ChestGui gui = new ChestGui(rows, "My orders");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, rows, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // ORDERS PANE

        OutlinePane ordersPane = new OutlinePane(1, 1, 7, rows);

        List<Order> orders;
        orders = buys;
        orders.addAll(sells);

        for (Order order : orders) {

            Material material = instance.getMarketManager().getBooks().get(order.getBookId()).getItem().getMaterial();

            GuiItem item = new GuiItem(MarketItems.getOrderItem(material, order));
            item.setAction(event -> {
                // TODO: claim goods
                openOrderSettingsPage(player, order);
            });
            ordersPane.addItem(item);

        }

        // EMPTY SLOTS

        for (int i = 0; i < 7; i++) {
            if (ordersPane.getItems().size() == rows * 7) break;
            ordersPane.addItem(new GuiItem(new ItemStack(Material.AIR)));
        }

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(4, rows - 1, 1, 1);
        GuiItem goBack = new GuiItem(MarketItems.getBackArrow("Market"));
        goBack.setAction(event -> openMarketPage(player));
        bottomPane.addItem(goBack, 0, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(ordersPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    private void openOrderSettingsPage(Player player, Order order) {

        player.closeInventory();

        // CREATE GUI + BACKGROUND

        ChestGui gui = new ChestGui(4, "Order options");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // OPTIONS PANE

        StaticPane optionsPane = new StaticPane(2, 1, 5, 0);

        if (order.isCancellable()) {

            GuiItem cancel = new GuiItem(MarketItems.getCancelOrderButton(order));
            cancel.setAction(event -> {
                OrderBook book = instance.getMarketManager().getBooks().get(order.getBookId());
                book.remove(order);
                player.closeInventory();
                Lang.ORDER_CANCELLED.send(player, "%AMOUNT%;" + order.getAmount(), "%ITEM%;" + book.getItem().getName());
            });
            optionsPane.addItem(cancel, 1, 0);

            GuiItem flip = new GuiItem(MarketItems.getFlipOrderButton(order));
            // TODO: set flip action
            optionsPane.addItem(flip, 5, 0);

        } else {

            GuiItem cancel = new GuiItem(MarketItems.getCannotCancelOrderButton());
            optionsPane.addItem(cancel, 3, 0);

        }

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
        GuiItem goBack = new GuiItem(MarketItems.getBackArrow("My orders"));
        goBack.setAction(event -> openMarketPage(player));
        bottomPane.addItem(goBack, 0, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(optionsPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    private void claimGoods(Player player, Order order) {

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
