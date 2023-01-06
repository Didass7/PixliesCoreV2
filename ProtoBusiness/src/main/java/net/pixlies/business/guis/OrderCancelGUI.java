package net.pixlies.business.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.pixlies.business.items.MarketGUIItems;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class OrderCancelGUI {
      public static void open(UUID uuid, Order order) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
      
            // Create GUI
            ChestGui gui = new ChestGui(4, MarketLang.MARKET + "§8Cancel Order");
            gui.setOnGlobalClick(event -> event.setCancelled(true));
      
            // Background pane
            StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
            background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
      
            // Cancel pane
            StaticPane cancelPane = new StaticPane(4, 1, 1, 1);
      
            GuiItem cancel = new GuiItem(MarketGUIItems.getCancelOrderButton(order));
            cancel.setAction(event -> {
                  OrderBook book = OrderBook.get(order.getBookItem());
                  book.remove(order);
      
                  SoundUtil.cancelledOrder(player);
                  MarketLang.ORDER_CANCELLED.send(player, "%AMOUNT%;" + order.getAmount(),
                          "%ITEM%;" + book.getItem().getName());
      
                  // Move this method somewhere else
                  // profile.refundGoods(order);
                  player.closeInventory();
            });
            cancelPane.addItem(cancel, 0, 0);
      
            // Bottom pane
            StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
            GuiItem goBack = new GuiItem(MarketGUIItems.getBackArrow("Placed Orders"));
            goBack.setAction(event -> OrdersListGUI.open(uuid));
            bottomPane.addItem(goBack, 0, 0);
      
            // Add panes
            gui.addPane(background);
            gui.addPane(cancelPane);
            gui.addPane(bottomPane);
      
            // Show GUI
            gui.show(player);
            gui.update();
      }
}
