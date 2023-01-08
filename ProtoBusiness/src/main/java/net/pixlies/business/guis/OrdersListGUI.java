package net.pixlies.business.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.pixlies.business.items.MarketGUIItems;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.util.MarketUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Անկեղծ ասած՝ դու բնավ էլ ա՛յն չես եղել
 * Ա՛յն չես եղել, ինչ որ ես եմ կարծել երկար։
 *
 * @author պարոյր սեւակ
 */
public class OrdersListGUI {
      public static void open(UUID uuid) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
      
            // Get number of rows
            List<Order> buys = MarketUtil.getPlayerBuyOrders(player.getUniqueId());
            List<Order> sells = MarketUtil.getPlayerSellOrders(player.getUniqueId());
            int rows = (int) Math.ceil(((buys.size() + sells.size()) / 7.0));
      
            // Get all orders
            List<Order> orders;
            orders = buys;
            orders.addAll(sells);
            
            // Separate GUI
            if (rows == 0) {
                  ChestGui gui = new ChestGui(1, MarketLang.MARKET + "§8Placed Orders - None");
                  gui.setOnGlobalClick(event -> event.setCancelled(true));
      
                  StaticPane pane = new StaticPane(0, 0, 9, 1, Pane.Priority.LOWEST);
                  pane.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
      
                  GuiItem goBack = new GuiItem(MarketGUIItems.getBackArrow("Market"));
                  goBack.setAction(event -> MarketInitialGUI.open(uuid));
                  pane.addItem(goBack, 4, 0);
                  
                  gui.addPane(pane);
                  gui.show(player);
                  gui.update();
            }
      
            // Create GUI
            ChestGui gui = new ChestGui(rows + 2, MarketLang.MARKET + "§8Placed Orders");
            gui.setOnGlobalClick(event -> event.setCancelled(true));
      
            // Background pane
            StaticPane background = new StaticPane(0, 0, 9, rows + 2, Pane.Priority.LOWEST);
            background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
      
            // Orders pane
            OutlinePane ordersPane = new OutlinePane(1, 1, 7, rows);
      
            // Fill with the order items
            for (Order order : orders) {
                  Material material = OrderBook.get(order.getBookItem()).getItem().getMaterial();
                  GuiItem item = new GuiItem(MarketGUIItems.getOrderItem(material, order));
                  item.setAction(event -> {
                        if (order.isCancellable()) {
                              OrderCancelGUI.open(uuid, order);
                        } else {
                              // Move this method somewhere else
                              // profile.claimGoods(order);
                        }
                  });
                  ordersPane.addItem(item);
            }
            
            // Fill empty slots with air
            for (int i = 0; i < 7; i++) {
                  if (ordersPane.getItems().size() == rows * 7) break;
                  ordersPane.addItem(new GuiItem(MarketGUIItems.getEmptyItem()));
            }
      
            // Bottom pane
            StaticPane bottomPane = new StaticPane(4, rows + 1, 1, 1);
      
            GuiItem goBack = new GuiItem(MarketGUIItems.getBackArrow("Market"));
            goBack.setAction(event -> MarketInitialGUI.open(uuid));
            bottomPane.addItem(goBack, 0, 0);
      
            // Add panes
            gui.addPane(background);
            gui.addPane(ordersPane);
            gui.addPane(bottomPane);
      
            // Show GUI
            gui.show(player);
            gui.update();
      }
}