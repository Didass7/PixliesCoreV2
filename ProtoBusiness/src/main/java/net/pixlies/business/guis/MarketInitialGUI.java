package net.pixlies.business.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.items.MarketGUIItems;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.OrderProfile;
import net.pixlies.business.guis.panes.MarketPane;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Լաց վերջին լացըդ, սիրտ իմ մենավոր։
 *
 * @author վահան դերյան
 */
public class MarketInitialGUI {
      public static void open(OrderProfile profile) {
            Player player = Bukkit.getPlayer(profile.getUUID());
            assert player != null;
      
            // Index 0 is the page currently being viewed
            // Index 1 is the page which was previously being viewed
            final Selection[] viewing = {
                    Selection.MINERALS,
                    Selection.MINERALS
            };
      
            // Create GUI
            ChestGui gui = new ChestGui(6, MarketLang.NAMESPACE);
            gui.setOnGlobalClick(event -> event.setCancelled(true));
      
            // Background pane
            StaticPane background = new StaticPane(0, 0, 9, 6, Pane.Priority.LOWEST);
            background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
      
            // Market pane
            MarketPane marketPane = new MarketPane(2, 0, 7, 5);
            marketPane.loadPage(Selection.MINERALS, profile.getUUID(), profile);
            background.addItem(new GuiItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE)), 1, 0);
            
            // Selection pane
            StaticPane selectionPane = new StaticPane(0, 0, 1, 6);
      
            for (Selection selection : Selection.values()) {
                  // Basic item stuff
                  GuiItem item = new GuiItem(selection == Selection.MINERALS ?
                          MarketGUIItems.getSelectedSelection(selection, selection.getName()) :
                          MarketGUIItems.getUnselectedSelection(selection, selection.getName())
                  );
            
                  // On item click
                  Consumer<InventoryClickEvent> selectionClick = new Consumer<>() {
                        @Override
                        public void accept(InventoryClickEvent inventoryClickEvent) {
                              // TODO: Gold standard menu
                              
                              if (selection == viewing[0]) return;
                              
                              viewing[1] = viewing[0];
                              viewing[0] = selection;
      
                              GuiItem unSelected = new GuiItem(
                                      MarketGUIItems.getUnselectedSelection(viewing[1], viewing[1].getName())
                              );
                              GuiItem selected = new GuiItem(
                                      MarketGUIItems.getSelectedSelection(viewing[0], viewing[0].getName())
                              );
                              
                              unSelected.setAction(this);
                              selected.setAction(this);
      
                              selectionPane.addItem(unSelected, 0, viewing[1].ordinal());
                              selectionPane.addItem(selected, 0, viewing[0].ordinal());
      
                              background.addItem(
                                      new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                                      1,
                                      viewing[1].ordinal()
                              );
                              
                              background.addItem(
                                      new GuiItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE)),
                                      1,
                                      viewing[0].ordinal()
                              );
      
                              marketPane.loadPage(viewing[0], profile.getUUID(), profile);
                              gui.update();
                        }
                  };
                  
                  item.setAction(selectionClick);
                  selectionPane.addItem(item, 0, selection.ordinal());
            }
      
            // Bottom pane
            StaticPane bottomPane = new StaticPane(3, 5, 4, 1);
      
            GuiItem myProfile = new GuiItem(MarketGUIItems.getProfileStats(player));
            bottomPane.addItem(myProfile, 0, 0);
      
            GuiItem marketStats = new GuiItem(MarketGUIItems.getMarketStats());
            bottomPane.addItem(marketStats, 1, 0);
      
            GuiItem myOrders = new GuiItem(MarketGUIItems.getMyOrdersButton(player));
            myOrders.setAction(event -> OrdersPageGUI.open(profile));
            bottomPane.addItem(myOrders, 3, 0);
      
            // Add panes
            gui.addPane(background);
            gui.addPane(marketPane);
            gui.addPane(selectionPane);
            gui.addPane(bottomPane);
      
            // Show GUI
            gui.show(player);
            gui.update();
      }
      
      @Getter
      @AllArgsConstructor
      public enum Selection {
            MINERALS(Material.DIAMOND_PICKAXE, "§b", false, true, false),
            FOODSTUFFS_AND_PLANTS(Material.GOLDEN_HOE, "§e", true, true, true),
            BLOCKS(Material.IRON_SHOVEL, "§d", true, true, true),
            MOB_DROPS(Material.NETHERITE_SWORD, "§c", false, true, false),
            MISCELLANEOUS(Material.ARROW, "§9", false, false, false),
            GOLD_STANDARD(Material.GOLD_INGOT, "§6", false, false, false);
      
            private final Material material;
            private final String color;
            private final boolean seventhColumn;
            private final boolean fourthRow;
            private final boolean fifthRow;
            
            public boolean hasSeventhColumn() {
                  return seventhColumn;
            }
            
            public boolean hasFourthRow() {
                  return fourthRow;
            }
            
            public boolean hasFifthRow() {
                  return fifthRow;
            }
            
            public String getName() {
                  return WordUtils.capitalize(name().toLowerCase().replace("_", " "));
            }
      }
}
