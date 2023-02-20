package net.pixlies.business.guis.panes;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.pixlies.business.guis.MarketInitialGUI;
import net.pixlies.business.guis.OrderItemGUI;
import net.pixlies.business.guis.items.MarketGUIItems;
import net.pixlies.business.market.OrderBook;
import net.pixlies.business.market.OrderItem;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MarketPane extends StaticPane {
    @NotNull
    private final Map<Map.Entry<Integer, Integer>, GuiItem> items;
    
    public MarketPane(int x, int y, int length, int height) {
        super(x, y, length, height);
        items = new HashMap<>(length * height);
    }
    
    public void loadPage(MarketInitialGUI.Selection page, UUID uuid) {
        fillWith(MarketGUIItems.getEmptyItem());
        
        if (!page.hasFourthRow()) {
            for (int x = 0; x < 7; x++) addItem(new GuiItem(MarketGUIItems.getEmptyItem()), x, 3);
        }
        
        if (!page.hasFifthRow()) {
            for (int x = 0; x < 7; x++) addItem(new GuiItem(MarketGUIItems.getEmptyItem()), x, 4);
        }
        
        if (!page.hasSeventhColumn()) {
            for (int y = 0; y < 5; y++)
                addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)), 6, y);
        }
        
        for (OrderItem item : OrderItem.getItemsOfPage(page.ordinal())) {
            OrderBook book = item.getBook();
            assert book != null;
            ItemBuilder builder = new ItemBuilder(item.getMaterial())
                    .setDisplayName(page.getColor() + item.getName())
                    .addLoreLine("§7Lowest buy offer: §6" + book.getLowestBuyPrice(uuid).doubleValue() + " coins")
                    .addLoreLine("§7Highest sell offer: §6" + book.getHighestSellPrice(uuid).doubleValue() + " coins")
                    .addLoreLine(" ")
                    .addLoreLine("§eClick to buy or sell!");
            GuiItem guiItem = new GuiItem(builder.build());
            guiItem.setAction(event -> OrderItemGUI.open(uuid, item));
            addItem(guiItem, item.getPosX(), item.getPosY());
        }
    }
}
