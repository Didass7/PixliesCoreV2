package net.pixlies.business.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.items.MarketGUIItems;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.conversations.AmountPrompt;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.profiles.OrderProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OrderItemGUI {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      public static void open(OrderProfile profile, OrderItem item) {
            Player player = Bukkit.getPlayer(profile.getUUID());
            OrderBook book = OrderBook.get(item);
      
            // Create GUI
            ChestGui gui = new ChestGui(4, MarketLang.MARKET + "§8" + item.getName());
            gui.setOnGlobalClick(event -> event.setCancelled(true));
      
            // Background pane
            StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
            background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
      
            // Item pane
            StaticPane itemPane = new StaticPane(4, 1, 1, 1);
            GuiItem orderItem = new GuiItem(new ItemStack(item.getMaterial()));
            itemPane.addItem(orderItem, 0, 0);
      
            // Transactions pane
            StaticPane transactionsPane = new StaticPane(1, 1, 7, 1);
      
            for (Transactions transaction : Transactions.values()) {
                  GuiItem guiItem = transaction.getGuiItem(player, item);
                  guiItem.setAction(event -> {
                        // Set temporary information in OrderProfile
                        profile.setSignStage((byte) 1);
                        profile.setTempOrder(new Order(
                                transaction.getType(),
                                book.getItem().name(),
                                System.currentTimeMillis(),
                                profile.getUUID(),
                                0,
                                0
                        ));
                        profile.setTempTitle(item.getName());
                        profile.save();
      
                        player.closeInventory();
                        
                        // Starts a chat conversation for the transaction item amount
                        ConversationFactory factory = new ConversationFactory(instance)
                                .withModality(true)
                                .withFirstPrompt(new AmountPrompt())
                                .withEscapeSequence("/quit")
                                .withTimeout(20)
                                .thatExcludesNonPlayersWithMessage("Go away evil console!");
                        factory.buildConversation(player).begin();
                  });
                  transactionsPane.addItem(guiItem, transaction.getX(), transaction.getY());
            }
      
            // Bottom pane
            StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
            GuiItem goBack = new GuiItem(MarketGUIItems.getBackArrow("Market"));
            goBack.setAction(event -> MarketInitialGUI.open(profile));
            bottomPane.addItem(goBack, 0, 0);
      
            // Add panes
            gui.addPane(background);
            gui.addPane(itemPane);
            gui.addPane(transactionsPane);
            gui.addPane(bottomPane);
      
            // Show GUI
            gui.show(player);
            gui.update();
      }
      
      @Getter
      @AllArgsConstructor
      public enum Transactions {
            BUY(Order.Type.BUY, 0, 0),
            SELL(Order.Type.SELL, 6, 0);
            
            private final Order.Type type;
            private final int x;
            private final int y;
            
            public GuiItem getGuiItem(Player player, OrderItem item) {
                  OrderProfile profile = OrderProfile.get(player.getUniqueId());
                  if (this == BUY) {
                        return new GuiItem(MarketGUIItems.getBuyButton(player.getUniqueId(), item));
                  } else {
                        assert profile != null;
                        return new GuiItem(MarketGUIItems.getSellButton(player.getUniqueId(), item,
                                profile.getItemAmount(item)));
                  }
            }
      }
}
