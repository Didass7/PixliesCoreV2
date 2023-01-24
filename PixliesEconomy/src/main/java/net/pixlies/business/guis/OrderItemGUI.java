package net.pixlies.business.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.PixliesEconomy;
import net.pixlies.business.conversations.AmountPrompt;
import net.pixlies.business.guis.items.MarketGUIItems;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Order;
import net.pixlies.business.market.OrderItem;
import net.pixlies.business.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class OrderItemGUI {
      private static final PixliesEconomy instance = PixliesEconomy.getInstance();
      
      public static void open(UUID uuid, OrderItem item) {
            Player player = Bukkit.getPlayer(uuid);
      
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
                        player.closeInventory();
                        
                        // Start a chat conversation for the transaction item amount
                        ConversationFactory factory = new ConversationFactory(instance)
                                .withFirstPrompt(new AmountPrompt())
                                .withEscapeSequence("quit")
                                .withTimeout(20)
                                .withLocalEcho(false)
                                .thatExcludesNonPlayersWithMessage("Go away evil console!");
                        Conversation conversation = factory.buildConversation(player);
                        conversation.getContext().setSessionData("uuid", player.getUniqueId());
                        conversation.getContext().setSessionData("type", transaction.getType());
                        conversation.getContext().setSessionData("item", item.name());
                        conversation.begin();
                  });
                  transactionsPane.addItem(guiItem, transaction.getX(), transaction.getY());
            }
      
            // Bottom pane
            StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
            GuiItem goBack = new GuiItem(MarketGUIItems.getBackArrow("Market"));
            goBack.setAction(event -> MarketInitialGUI.open(uuid));
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
                  if (this == BUY) {
                        return new GuiItem(MarketGUIItems.getBuyButton(player.getUniqueId(), item));
                  } else {
                        return new GuiItem(MarketGUIItems.getSellButton(
                                player.getUniqueId(),
                                item,
                                InventoryUtil.getItemAmount(player.getUniqueId(), item)
                        ));
                  }
            }
      }
}
