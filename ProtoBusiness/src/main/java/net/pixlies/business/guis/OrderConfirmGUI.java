package net.pixlies.business.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.pixlies.business.guis.items.MarketGUIItems;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Order;
import net.pixlies.business.market.OrderBook;
import net.pixlies.business.market.OrderItem;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class OrderConfirmGUI {
      public static void open(UUID uuid, Order.Type type, OrderItem item, int amount, double price) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
      
            OrderBook book = OrderBook.get(item);
            
            NationProfile profile = NationProfile.get(player.getUniqueId());
            double tax = 0;
            if (profile.getNation() != null) {
                  tax = profile.getNation().getTaxRate();
            }
            
            // Create GUI
            ChestGui gui = new ChestGui(4, MarketLang.MARKET + "§8Confirm Order");
            gui.setOnGlobalClick(event -> event.setCancelled(true));
      
            // Background pane
            StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
            background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            
            // Order
            Order order = new Order(type, book.getItem().name(), System.currentTimeMillis(), uuid, price, amount);
      
            // Confirm pane
            StaticPane confirmPane = new StaticPane(4, 1, 1, 1);
      
            GuiItem confirm = new GuiItem(MarketGUIItems.getConfirmOrderButton(order, tax));
            confirm.setAction(event -> {
                  // Item and money actions
                  switch (order.getType()) {
                        case BUY -> {
                              profile.removeBalance(order.getTaxedPrice() * order.getAmount());
                              profile.save();
                              book.buy(order);
                        }
                        case SELL -> {
                              player.getInventory().removeItemAnySlot(new ItemStack(item.getMaterial(), amount));
                              player.updateInventory();
                              book.sell(order);
                        }
                  }
                  
                  // Message and sound
                  MarketLang.NEW_ORDER_CREATED.send(player, "%ORDER%;" + order.toString());
                  SoundUtil.placedOrder(player);
                  
                  player.closeInventory();
            });
            confirmPane.addItem(confirm, 0, 0);
      
            // Bottom pane
            StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
            GuiItem goBack = new GuiItem(MarketGUIItems.getBackArrow(type.name() + ": " + item.getName()));
            goBack.setAction(event -> OrderPriceGUI.open(
                    uuid,
                    order.getType(),
                    OrderItem.valueOf(order.getBookItem()), order.getAmount())
            );
            bottomPane.addItem(goBack, 0, 0);
      
            // Add panes
            gui.addPane(background);
            gui.addPane(confirmPane);
            gui.addPane(bottomPane);
      
            // Show GUI
            gui.show(player);
            gui.update();
      }
}
