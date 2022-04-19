package net.pixlies.business.panes;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.commands.impl.MarketCommand;
import net.pixlies.business.market.orders.OrderProfile;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MarketPane extends StaticPane {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @NotNull private final Map<Map.Entry<Integer, Integer>, GuiItem> items;

    public MarketPane(int x, int y, int length, int height) {
        super(x, y, length, height);
        items = new HashMap<>(length * height);
    }

    public void loadPage(MarketCommand.Selection page, UUID uuid) {
        fillWith(new ItemStack(Material.AIR));
        if (!page.hasSeventhRow()) {
            for (int y = 0; y < 6; y++) addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)), 6, y);
        }

        for (OrderItem item : OrderItem.getItemsOfPage(page.ordinal())) {
            OrderBook book = item.getBook();
            assert book != null;
            ItemBuilder builder = new ItemBuilder(item.getMaterial())
                    .setDisplayName(page.getColor() + item.getName())
                    .addLoreLine("§7Lowest buy offer: §6" + book.getLowestBuyPrice() + " coins")
                    .addLoreLine("§7Highest sell offer: §6" + book.getHighestSellPrice() + " coins")
                    .addLoreLine(" ")
                    .addLoreLine("§eClick to buy or sell!");
            GuiItem guiItem = new GuiItem(builder.build());
            guiItem.setAction(event -> {
                User user = User.get(uuid);
                OrderProfile profile = (OrderProfile) user.getExtras().get("orderProfile");
                profile.openItemPage(item);
            });
            addItem(guiItem, item.getPosX(), item.getPosY());
        }
    }

}
