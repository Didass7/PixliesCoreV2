package net.pixlies.business.market;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.commands.impl.MarketCommand;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.orders.Trade;
import net.pixlies.business.panes.MarketPane;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Market profile
 *
 * @author vPrototype_
 */
@Data
public class MarketProfile {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    private final UUID uuid;
    private Order tempOrder;
    private String tempTitle;
    private byte signStage;

    public MarketProfile(UUID uuid) {
        this.uuid = uuid;
    }

    // ----------------------------------------------------------------------------------------------------
    // GUI METHODS
    // ----------------------------------------------------------------------------------------------------

    public void openMarketPage() {

        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        // Index 0 is the page currently being viewed, index 1 is the page which was previously being viewed
        final MarketCommand.Selection[] viewing = { MarketCommand.Selection.MINERALS, MarketCommand.Selection.MINERALS };

        // CREATE GUI + BACKGROUND

        ChestGui gui = new ChestGui(6, "Market");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // MARKET PANE

        MarketPane marketPane = new MarketPane(2, 0, 7, 5);
        marketPane.loadPage(MarketCommand.Selection.MINERALS, uuid);

        // SELECTION PANE

        StaticPane selectionPane = new StaticPane(0, 0, 1, 7);

        for (MarketCommand.Selection s : MarketCommand.Selection.values()) {

            // ITEM STUFF

            GuiItem item = new GuiItem(s == MarketCommand.Selection.MINERALS ? MarketItems.getSelectedSelection(s, s.getName()) :
                    MarketItems.getUnselectedSelection(s, s.getName()));

            // ON ITEM CLICK

            item.setAction(event -> {

                // SELECTIONS

                if (s == viewing[0]) return;
                viewing[1] = viewing[0];
                viewing[0] = s;

                // BUTTON TEXT

                selectionPane.addItem(new GuiItem(MarketItems.getUnselectedSelection(viewing[1], viewing[1].getName())),
                        0, viewing[1].ordinal());
                selectionPane.addItem(new GuiItem(MarketItems.getSelectedSelection(viewing[0], viewing[0].getName())),
                        0, viewing[0].ordinal());

                marketPane.loadPage(viewing[0], uuid);
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
        myOrders.setAction(event -> openOrdersPage());
        bottomPane.addItem(myOrders, 3, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(marketPane);
        gui.addPane(selectionPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    public void openOrdersPage() {

        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        List<Order> buys = instance.getMarketManager().getPlayerBuyOrders(player.getUniqueId());
        List<Order> sells = instance.getMarketManager().getPlayerSellOrders(player.getUniqueId());
        int rows = (int) Math.round(((buys.size() + sells.size()) / 7.0) + 0.5);

        // CREATE GUI + BACKGROUND

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
                if (order.isCancellable()) {
                    openOrderSettingsPage(order);
                } else {
                    claimGoods(order);
                }
            });
            ordersPane.addItem(item);
        }

        // FILL EMPTY SLOTS

        for (int i = 0; i < 7; i++) {
            if (ordersPane.getItems().size() == rows * 7) break;
            ordersPane.addItem(new GuiItem(new ItemStack(Material.AIR)));
        }

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(4, rows - 1, 1, 1);

        GuiItem goBack = new GuiItem(MarketItems.getBackArrow("Market"));
        goBack.setAction(event -> openMarketPage());
        bottomPane.addItem(goBack, 0, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(ordersPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    public void openOrderSettingsPage(Order order) {

        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        // CREATE GUI + BACKGROUND

        ChestGui gui = new ChestGui(4, "Order options");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // CANCEL PANE

        StaticPane cancelPane = new StaticPane(4, 1, 1, 1);

        GuiItem cancel = new GuiItem(MarketItems.getCancelOrderButton(order));
        cancel.setAction(event -> {
            OrderBook book = instance.getMarketManager().getBooks().get(order.getBookId());
            book.remove(order);

            player.playSound(player.getLocation(), "block.netherite_block.place", 100, 1);
            Lang.ORDER_CANCELLED.send(player, "%AMOUNT%;" + order.getAmount(), "%ITEM%;" + book.getItem().getName());

            refundGoods(order);
        });
        cancelPane.addItem(cancel, 0, 0);

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
        GuiItem goBack = new GuiItem(MarketItems.getBackArrow("My orders"));
        goBack.setAction(event -> openOrdersPage());
        bottomPane.addItem(goBack, 0, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(cancelPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    public void openItemPage(OrderItem item) {

        Player player = Bukkit.getPlayer(uuid);
        OrderBook book = instance.getMarketManager().getBookFromItem(item);
        assert player != null;

        // CREATE GUI + BACKGROUND

        ChestGui gui = new ChestGui(4, item.getName());
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // ITEM PANE

        StaticPane itemPane = new StaticPane(4, 1, 1, 1);

        GuiItem orderItem = new GuiItem(new ItemStack(item.getMaterial()));
        itemPane.addItem(orderItem, 0, 0);

        // TRANSACTIONS PANE

        StaticPane transactionsPane = new StaticPane(1, 1, 7, 1);

        for (ItemOptions i : ItemOptions.values()) {
            GuiItem guiItem = i.getGuiItem(item);
            assert guiItem != null;
            guiItem.setAction(event -> {
                User user = User.get(player.getUniqueId());
                MarketProfile profile = MarketProfile.get(user);
                assert profile != null;
                Order temp = new Order(i.getType(), book.getBookId(), System.currentTimeMillis(), i.isLimit(), uuid, 0, 0);
                profile.setTempOrder(temp);
                profile.setTempTitle(item.getName());
                
                player.getWorld().getBlockAt(player.getLocation()).setType(Material.BIRCH_WALL_SIGN);
                Sign sign = (Sign) player.getWorld().getBlockAt(player.getLocation()).getState();
                sign.line(1, Component.text("^^ -------- ^^"));
                sign.line(2, Component.text("Set an amount"));
                sign.line(3, Component.text("(integer)"));
                sign.update();
            });
            transactionsPane.addItem(guiItem, i.getX(), i.getY());
        }

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
        GuiItem goBack = new GuiItem(MarketItems.getBackArrow("Market"));
        goBack.setAction(event -> openMarketPage());
        bottomPane.addItem(goBack, 0, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(itemPane);
        gui.addPane(transactionsPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    public void openPricePage(OrderItem item, Order.OrderType type, boolean limit, int amount) {

        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        OrderBook book = instance.getMarketManager().getBookFromItem(item);

        // PAGE TITLE

        String pageTitle = null;
        switch (type) {
            case BUY -> {
                if (limit) pageTitle = "Limit buy: " + item.getName();
                else pageTitle = "Market buy: " + item.getName();
            }
            case SELL -> {
                if (limit) pageTitle = "Limit sell: " + item.getName();
                else pageTitle = "Market sell: " + item.getName();
            }
        }

        // CREATE GUI + BACKGROUND

        ChestGui gui = new ChestGui(4, pageTitle);
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // PRICES PANE

        StaticPane pricesPane = new StaticPane(2, 1, 5, 0);

        boolean emptyBuyCondition = type == Order.OrderType.BUY && book.getBuyOrders().isEmpty();
        boolean emptySellCondition = type == Order.OrderType.SELL && book.getSellOrders().isEmpty();
        String finalPageTitle = pageTitle;

        GuiItem customPrice = new GuiItem(MarketItems.getCustomPriceButton());
        customPrice.setAction(event -> {
            User user = User.get(player.getUniqueId());
            MarketProfile profile = MarketProfile.get(user);
            assert profile != null;
            profile.setTempOrder(new Order(type, book.getBookId(), System.currentTimeMillis(), limit, player.getUniqueId(),
                    0.0, amount));
            profile.setTempTitle(finalPageTitle);

            player.getWorld().getBlockAt(player.getLocation()).setType(Material.BIRCH_WALL_SIGN);
            Sign sign = (Sign) player.getWorld().getBlockAt(player.getLocation()).getState();
            sign.line(1, Component.text("^^ -------- ^^"));
            sign.line(2, Component.text("Set a custom"));
            sign.line(3, Component.text("price"));
            sign.update();
        });

        if (emptyBuyCondition || emptySellCondition) {
            pricesPane.addItem(customPrice, 2, 0);
        } else {
            GuiItem marketPrice = new GuiItem(MarketItems.getBestPriceButton(item, type));
            marketPrice.setAction(event -> {
                double price = type == Order.OrderType.BUY ? book.getLowestBuyPrice() : book.getHighestSellPrice();
                Order order = new Order(type, book.getBookId(), System.currentTimeMillis(), limit, player.getUniqueId(),
                        price, amount);
                openConfirmOrderPage(order, finalPageTitle);
            });
            pricesPane.addItem(marketPrice, 0, 0);

            GuiItem changedPrice = new GuiItem(MarketItems.getChangedPriceButton(item, type));
            changedPrice.setAction(event -> {
                double price = type == Order.OrderType.BUY ? book.getLowestBuyPrice() + 0.1 :
                        book.getHighestSellPrice() - 0.1;
                Order order = new Order(type, book.getBookId(), System.currentTimeMillis(), limit, player.getUniqueId(),
                        price, amount);
                openConfirmOrderPage(order, finalPageTitle);
            });
            pricesPane.addItem(changedPrice, 2, 0);

            pricesPane.addItem(customPrice, 4, 0);
        }

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
        GuiItem goBack = new GuiItem(MarketItems.getBackArrow(item.getName()));
        goBack.setAction(event -> openItemPage(item));
        bottomPane.addItem(goBack, 0, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(pricesPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    public void openConfirmOrderPage(Order order, String previous) {

        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        OrderBook book = instance.getMarketManager().getBooks().get(order.getBookId());
        String itemName = book.getItem().getName();

        // CREATE GUI + BACKGROUND

        ChestGui gui = new ChestGui(4, "Confirm order");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // CONFIRM PANE

        StaticPane confirmPane = new StaticPane(4, 1, 1, 1);

        GuiItem confirm = new GuiItem(MarketItems.getConfirmOrderButton(order));
        confirm.setAction(event -> {
            switch (order.getOrderType()) {
                case BUY -> {
                    book.buy(order);
                    Lang.BUY_ORDER_CREATED.send(player, "%AMOUNT%;" + order.getAmount(), "%ITEM%;" + itemName);
                }
                case SELL -> {
                    book.sell(order);
                    Lang.SELL_ORDER_CREATED.send(player, "%AMOUNT%;" + order.getAmount(), "%ITEM%;" + itemName);
                }
            }
            player.playSound(player.getLocation(), "block.amethyst_block.break", 100, 1);
        });
        confirmPane.addItem(confirm, 0, 0);

        // BOTTOM PANE

        StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
        GuiItem goBack = new GuiItem(MarketItems.getBackArrow(previous));
        goBack.setAction(event -> openPricePage(book.getItem(), order.getOrderType(), order.isLimitOrder(),
                order.getAmount()));
        bottomPane.addItem(goBack, 0, 0);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(confirmPane);
        gui.addPane(bottomPane);

        gui.show(player);
        gui.update();

    }

    // ----------------------------------------------------------------------------------------------------
    // OTHER METHODS
    // ----------------------------------------------------------------------------------------------------

    private void refundGoods(Order order) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        OrderBook book = instance.getMarketManager().getBooks().get(order.getBookId());
        if (order.getOrderType() == Order.OrderType.BUY) {
            Material material = book.getItem().getMaterial();
            for (int i = 0; i < order.getVolume(); i++) player.getInventory().addItem(new ItemStack(material));
            Lang.ORDER_ITEMS_REFUNDED.send(player, "%AMOUNT%;" + order.getVolume(), "%ITEM%;" + book.getItem().getName());
        } else {
            User user = User.get(player.getUniqueId());
            // TODO: add coins to wallet
            Lang.ORDER_COINS_REFUNDED.send(player, "%COINS%" + (order.getVolume() * order.getPrice()));
        }
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
    }

    private void claimGoods(Order order) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        OrderBook book = instance.getMarketManager().getBooks().get(order.getBookId());
        if (order.getOrderType() == Order.OrderType.BUY) {
            int items = 0;
            for (Trade t : order.getTrades()) {
                if (t.isClaimed()) continue;
                items += t.getAmount();
                t.claim();
            }

            Material material = book.getItem().getMaterial();
            for (int i = 0; i < items; i++) player.getInventory().addItem(new ItemStack(material));

            Lang.ORDER_ITEMS_CLAIMED.send(player, "%NUM%;" + items, "%AMOUNT%;" + order.getAmount(),
                    "%ITEM%;" + book.getItem().getName());
        } else {
            int coins = 0;
            for (Trade t : order.getTrades()) {
                if (t.isClaimed()) continue;
                coins += t.getAmount() * t.getPrice();
                t.claim();
            }

            User user = User.get(player.getUniqueId());
            // TODO: add coins to wallet

            Lang.ORDER_ITEMS_CLAIMED.send(player, "%COINS%" + coins, "%AMOUNT%;" + order.getAmount(),
                    "%ITEM%;" + book.getItem().getName());
        }
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
    }

    // ----------------------------------------------------------------------------------------------------
    // STATIC METHODS
    // ----------------------------------------------------------------------------------------------------

    public static boolean hasProfile(@NotNull User user) {
        return user.getExtras().containsKey("marketProfile");
    }

    public static MarketProfile get(@NotNull User user) {
        if (!hasProfile(user)) return null;
        return (MarketProfile) user.getExtras().get("marketProfile");
    }

    // ----------------------------------------------------------------------------------------------------
    // ITEM OPTIONS ENUM
    // ----------------------------------------------------------------------------------------------------

    @Getter
    @AllArgsConstructor
    public enum ItemOptions {
        LIMIT_SELL(Order.OrderType.SELL, true, 0, 0),
        MARKET_SELL(Order.OrderType.SELL, false, 2, 0),
        MARKET_BUY(Order.OrderType.BUY, false, 5, 0),
        LIMIT_BUY(Order.OrderType.BUY, true, 6, 0);

        private final Order.OrderType type;
        private final boolean limit;
        private final int x;
        private final int y;

        public GuiItem getGuiItem(OrderItem item) {
            switch (this) {
                case LIMIT_SELL -> {
                    return new GuiItem(MarketItems.getLimitSellButton(item));
                }
                case MARKET_SELL -> {
                    return new GuiItem(MarketItems.getMarketSellButton(item));
                }
                case MARKET_BUY -> {
                    return new GuiItem(MarketItems.getMarketBuyButton(item));
                }
                case LIMIT_BUY -> {
                    return new GuiItem(MarketItems.getLimitBuyButton(item));
                }
            }
            return null;
        }
    }

}