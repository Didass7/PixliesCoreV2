package net.pixlies.business.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.pixlies.business.PixliesEconomy;
import net.pixlies.business.conversations.CustomPricePrompt;
import net.pixlies.business.guis.items.MarketGUIItems;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Order;
import net.pixlies.business.market.OrderBook;
import net.pixlies.business.market.OrderItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class OrderPriceGUI {
      private static final PixliesEconomy instance = PixliesEconomy.getInstance();
      
      public static void open(UUID uuid, Order.Type type, OrderItem item, int amount) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            
            OrderBook book = OrderBook.get(item);
      
            // Page title
            String pageTitle = MarketLang.MARKET + "§8" + type.name() + ": " + item.getName();
      
            // Create GUI
            ChestGui gui = new ChestGui(4, pageTitle);
            gui.setOnGlobalClick(event -> event.setCancelled(true));
      
            // Background pane
            StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
            background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
      
            // Prices pane
            StaticPane pricesPane = new StaticPane(2, 1, 5, 1);
      
            boolean emptyBuyCondition = type == Order.Type.BUY && book.getBuyOrders().isEmpty();
            boolean emptySellCondition = type == Order.Type.SELL && book.getSellOrders().isEmpty();
      
            // Custom price item
            GuiItem customPrice = new GuiItem(MarketGUIItems.getCustomPriceButton());
            customPrice.setAction(event -> {
                  player.closeInventory();
      
                  // Start a chat conversation for the transaction unit price
                  ConversationFactory factory = new ConversationFactory(instance)
                          .withFirstPrompt(new CustomPricePrompt())
                          .withEscapeSequence("quit")
                          .withTimeout(20)
                          .withLocalEcho(false)
                          .thatExcludesNonPlayersWithMessage("Go away evil console!");
                  Conversation conversation = factory.buildConversation(player);
                  conversation.getContext().setSessionData("uuid", player.getUniqueId());
                  conversation.getContext().setSessionData("type", type);
                  conversation.getContext().setSessionData("item", item.name());
                  conversation.getContext().setSessionData("amount", amount);
                  conversation.begin();
            });
      
            // Check if we should put market price options
            if (emptyBuyCondition || emptySellCondition) {
                  pricesPane.addItem(customPrice, 2, 0);
            } else {
                  // Market price item
                  GuiItem marketPrice = new GuiItem(MarketGUIItems.getBestPriceButton(uuid, item, type, amount));
                  marketPrice.setAction(event -> {
                        double price = 0;
                        switch (type) {
                              case BUY -> price = book.getLowestBuyPrice(uuid);
                              case SELL -> price = book.getHighestSellPrice(uuid);
                        }
                        OrderConfirmGUI.open(uuid, type, item, amount, price);
                  });
                  pricesPane.addItem(marketPrice, 0, 0);
            
                  // Optimal price item
                  GuiItem changedPrice = new GuiItem(MarketGUIItems.getChangedPriceButton(uuid, item, type, amount));
                  changedPrice.setAction(event -> {
                        double price = 0;
                        switch (type) {
                              case BUY -> price = book.getLowestBuyPrice(uuid) + 0.1;
                              case SELL -> price = book.getHighestSellPrice(uuid) - 0.1;
                        }
                        OrderConfirmGUI.open(uuid, type, item, amount, price);
                  });
                  pricesPane.addItem(changedPrice, 2, 0);
            
                  // Custom price item
                  pricesPane.addItem(customPrice, 4, 0);
            }
      
            // Bottom pane
            StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
            GuiItem goBack = new GuiItem(MarketGUIItems.getBackArrow(item.getName()));
            goBack.setAction(event -> OrderItemGUI.open(uuid, item));
            bottomPane.addItem(goBack, 0, 0);
      
            // Add panes
            gui.addPane(background);
            gui.addPane(pricesPane);
            gui.addPane(bottomPane);
      
            // Show GUI
            gui.show(player);
            gui.update();
      }
}
