package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.ToggleButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("market|m|nasdaq|nyse|snp500")
@CommandPermission("pixlies.business.market")
public class MarketCommand extends BaseCommand {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);

    @Default
    @Description("Opens the market menu")
    public void onMarket(Player player) {
        User user = User.get(player.getUniqueId());

        // Create GUI
        ChestGui gui = new ChestGui(6, "Market");

        // Disable clicks
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // Background
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        // Selection pane
        StaticPane selectionPane = new StaticPane(0, 0, 1, 6);
        List<ToggleButton> selectionButtons = new ArrayList<>();
        for (Selection s : Selection.values()) {
            String name = StringUtils.capitalize(s.name().toLowerCase().replace(" ", "and"));
            Material material = s.getMaterial();

            ItemBuilder builder = new ItemBuilder(material).addLoreLine(" ");
            ToggleButton button = new ToggleButton(0, s.ordinal(), 1, 1);
            selectionButtons.add(button);

            if (s == Selection.MINERALS) {
                button.toggle();
                builder.setDisplayName("§b" + name).addLoreLine("§aYou are viewing this tab!").setGlow(true);
            } else builder.setDisplayName("§7" + name).addLoreLine("§eClick to view this tab!").setGlow(false);

            GuiItem item = new GuiItem(builder.build());
            item.setAction(event -> {
                if (button.isEnabled()) return;

                // Disables the other enabled button
                for (ToggleButton b : selectionButtons) {
                    if (b.isEnabled()) {
                        b.toggle();
                        break;
                    }
                }

                // GUI shenanigans
                button.toggle();
                builder.setDisplayName("§b" + name).addLoreLine("§aYou are viewing this tab!").setGlow(true);
                selectionPane.removeItem(0, s.ordinal());
                selectionPane.addItem(new GuiItem(builder.build()), 0, s.ordinal());
                gui.update();

                // TODO switch to the correct market page
            });

            selectionPane.addItem(item, 0, s.ordinal());
        }

        // Market pane
        StaticPane marketPane = new StaticPane(2, 0, 7, 5);
        // TODO market pane

        // BottomBar pane
        StaticPane bottomBarPane = new StaticPane(2, 5, 4, 1);
        // TODO bottom bar pane

        // Add panes
        gui.addPane(background);
        gui.addPane(selectionPane);
        gui.addPane(marketPane);
        gui.addPane(bottomBarPane);

        // Show GUI
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
