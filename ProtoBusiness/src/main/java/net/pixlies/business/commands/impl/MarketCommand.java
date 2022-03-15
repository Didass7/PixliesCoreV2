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
import net.pixlies.business.market.OrderItem;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.data.UserStats;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.core.utils.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicReference;

@CommandAlias("market|m|nasdaq|nyse|snp500")
@CommandPermission("pixlies.business.market")
public class MarketCommand extends BaseCommand {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);

    @Default
    @Description("Opens the market menu")
    public void onMarket(Player player) {
        User user = User.get(player.getUniqueId());
        UserStats stats = user.getStats();
        final Selection[] viewing = { Selection.MINERALS };

        // CREATE GUI + BACKGROUND

        ChestGui gui = new ChestGui(6, "Market");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        // MARKET PANE

        AtomicReference<StaticPane> marketPane = new AtomicReference<>(getMarketPane(viewing[0]));

        // SELECTION PANE

        StaticPane selectionPane = new StaticPane(0, 0, 1, 6);
        String selected = "§aYou are viewing this tab!";
        String notSelected = "§eClick to view this tab!";

        for (Selection s : Selection.values()) {

            // ITEM STUFF

            String name = StringUtils.capitalize(s.name().toLowerCase().replace("_", " "));
            ItemBuilder builder = new ItemBuilder(s.getMaterial())
                    .setDisplayName("§b" + name)
                    .addLoreLine(" ")
                    .addLoreLine(selected)
                    .setGlow(true)
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            GuiItem item = new GuiItem(builder.build());

            // ON ITEM CLICK

            item.setAction(event -> {

                if (s == viewing[0]) return;

                // DISABLING THE PREVIOUS BUTTON

                builder.setDisplayName("§7" + name)
                        .removeLoreLine(selected)
                        .addLoreLine(notSelected)
                        .setGlow(false);
                selectionPane.addItem(new GuiItem(builder.build()), 0, s.ordinal());

                // ENABLING THE CLICKED BUTTON

                builder.setDisplayName("§b" + name)
                        .removeLoreLine(notSelected)
                        .addLoreLine(selected)
                        .setGlow(true);
                selectionPane.addItem(new GuiItem(builder.build()), 0, Math.floorDiv(event.getSlot(), 9));

                // SHOWING THE NEW PANE

                marketPane.set(getMarketPane(viewing[0]));
                viewing[0] = s;
                gui.update();

            });

            selectionPane.addItem(item, 0, s.ordinal());

        }

        // BOTTOM BAR PANE

        StaticPane bottomBarPane = new StaticPane(2, 5, 4, 1);

        // PROFILE STATS

        ItemBuilder myProfileBuilder = new ItemBuilder(PlayerUtils.getSkull(player.getUniqueId()))
                .setDisplayName("§6" + player.getName() + "'s stats")
                .addLoreLine(" ")
                .addLoreLine("§7Buy orders made: §b" + stats.getBuyOrdersMade())
                .addLoreLine("§7Sell orders made: §b" + stats.getSellOrdersMade())
                .addLoreLine("§7Money spent: §b" + stats.getMoneySpent())
                .addLoreLine("§7Money gained: §b" + stats.getMoneyGained());
        GuiItem myProfile = new GuiItem(myProfileBuilder.build());
        selectionPane.addItem(myProfile, 3, 5);

        // MARKET STATS

        ItemBuilder marketStatsBuilder = new ItemBuilder(new ItemStack(Material.EMERALD))
                .setDisplayName("§aMarket stats")
                .addLoreLine(" ")
                .addLoreLine("§7Buy orders made: §a" + instance.getStats().get("market.buyOrders", 0))
                .addLoreLine("§7Sell orders made: §a" + instance.getStats().get("market.sellOrders", 0))
                .addLoreLine("§7Money spent: §a" + instance.getStats().get("market.moneySpent", 0))
                .addLoreLine("§7Money gained: §a" + instance.getStats().get("market.moneyGained", 0));
        GuiItem marketStats = new GuiItem(marketStatsBuilder.build());
        selectionPane.addItem(marketStats, 4, 5);

        // MY ORDERS

        ItemBuilder myOrdersBuilder = new ItemBuilder(new ItemStack(Material.BOOK))
                .setDisplayName("§bMy orders")
                .addLoreLine(" ")
                .addLoreLine("§eClick to view your orders!");
        GuiItem myOrders = new GuiItem(myOrdersBuilder.build());
        myOrders.setAction(event -> openOrdersPane(player));
        selectionPane.addItem(myOrders, 3, 5);

        // ADD PANES + SHOW GUI

        gui.addPane(background);
        gui.addPane(marketPane.get());
        gui.addPane(selectionPane);
        gui.addPane(bottomBarPane);

        gui.show(player);
        gui.update();
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
        for (OrderItem item : OrderItem.getItemsOfPage(selection.ordinal())) {
            String name = StringUtils.capitalize(item.name().toLowerCase().replace("_", " "));
            ItemBuilder builder = new ItemBuilder(item.getMaterial())
                    .setDisplayName("§b" + name)
                    .addLoreLine("§7Lowest buy offer: §6" + "$") // TODO lowest buy price
                    .addLoreLine("§7Lowest sell offer: §6" + "$") // TODO lowest sell price
                    .addLoreLine(" ")
                    .addLoreLine("§eClick to buy or sell");
            // TODO on item click
            pane.addItem(new GuiItem(builder.build()), item.getPosX(), item.getPosY());
        }
        return pane;
    }

    // ----------------------------------------------------------------------------------------------------

    private void openOrdersPane(Player player) {
        // TODO
    }

    // ----------------------------------------------------------------------------------------------------

    @AllArgsConstructor
    public enum Selection {
        MINERALS(Material.DIAMOND_PICKAXE),
        FOODSTUFFS_AND_PLANTS(Material.GOLDEN_HOE),
        BLOCKS(Material.IRON_SHOVEL),
        MOB_DROPS(Material.NETHERITE_SWORD),
        MISCELLANEOUS(Material.ARROW),
        STOCKS_AND_BONDS(Material.PAPER);

        @Getter private final Material material;
    }

}
