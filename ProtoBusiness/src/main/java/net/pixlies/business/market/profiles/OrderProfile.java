package net.pixlies.business.market.profiles;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.guis.OrderPriceGUI;
import net.pixlies.business.guis.OrdersListGUI;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.items.MarketGUIItems;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.orders.Trade;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

/**
 * Market profile.
 *
 * @author vyketype
 */
@Data
public class OrderProfile {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    private static final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);
    
    @Getter(AccessLevel.NONE)
    private final UUID uuid;
    
    private Order tempOrder;
    
    public OrderProfile(UUID uuid) {
        this.uuid = uuid;
    }
    
    public UUID getUUID() {
        return uuid;
    }
    
    public int getItemAmount(OrderItem item) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        Inventory inv = player.getInventory();
        int num = 0;
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) continue;
            if (Objects.equals(Objects.requireNonNull(inv.getItem(i)).getType(), item.getMaterial())) {
                num += Objects.requireNonNull(inv.getItem(i)).getAmount();
            }
        }
        return num;
    }
    
    // ----------------------------------------------------------------------------------------------------
    // GUI METHODS
    // ----------------------------------------------------------------------------------------------------
    
    public void openConfirmOrderPage(Order order, String previous) {
        
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        
        OrderBook book = OrderBook.get(order.getBookItem());
        String itemName = book.getItem().getName();
        
        // CREATE GUI + BACKGROUND
        
        ChestGui gui = new ChestGui(4, "Confirm order");
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        
        StaticPane background = new StaticPane(0, 0, 9, 4, Pane.Priority.LOWEST);
        background.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        
        Nation nation = NationProfile.get(player.getUniqueId()).getNation();
        double tax = nation.getTaxRate();
        
        // CONFIRM PANE
        
        StaticPane confirmPane = new StaticPane(4, 1, 1, 1);
        
        GuiItem confirm = new GuiItem(MarketGUIItems.getConfirmOrderButton(order, tax));
        confirm.setAction(event -> {
            switch (order.getType()) {
                case BUY -> {
                    double price = order.getAmount() * order.getPrice() * (1 + tax);
                    // TODO: take money from wallet
                    
                    book.buy(order);
                    MarketLang.BUY_ORDER_CREATED.send(player, "%AMOUNT%;" + order.getAmount(),
                            "%ITEM%;" + itemName);
                }
                case SELL -> {
                    OrderItem item = book.getItem();
                    player.getInventory().removeItemAnySlot(new ItemStack(item.getMaterial(),
                            getItemAmount(item)));
                    
                    book.sell(order);
                    MarketLang.SELL_ORDER_CREATED.send(player, "%AMOUNT%;" + order.getAmount(),
                            "%ITEM%;" + itemName);
                }
            }
            player.playSound(player.getLocation(), "block.amethyst_block.break", 100, 1);
            OrdersListGUI.open(uuid);
        });
        confirmPane.addItem(confirm, 0, 0);
        
        // BOTTOM PANE
        
        StaticPane bottomPane = new StaticPane(4, 3, 1, 1);
        GuiItem goBack = new GuiItem(MarketGUIItems.getBackArrow(previous));
        goBack.setAction(event -> OrderPriceGUI.open(uuid, order.getType(), OrderItem.valueOf(order.getBookItem()), order.getAmount()));
        bottomPane.addItem(goBack, 0, 0);
        
        // ADD PANES + SHOW GUI
        
        gui.addPane(background);
        gui.addPane(confirmPane);
        gui.addPane(bottomPane);
        
        gui.show(player);
        gui.update();
        
    }
    
    // ----------------------------------------------------------------------------------------------------
    // OTHER METHODS
    // ----------------------------------------------------------------------------------------------------
    
    // TODO: REFUNDING EXTRA MONEY + DON'T FORGET TO UPDATE THE BOOLEAN ON CLAIMING THE REFUND
    
    public void refundGoods(Order order) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        User user = User.get(uuid);
        OrderBook book = OrderBook.get(order.getBookItem());
        if (order.getType() == Order.Type.BUY) {
            Material material = book.getItem().getMaterial();
            for (int i = 0; i < order.getVolume(); i++) player.getInventory().addItem(new ItemStack(material));
            MarketLang.ORDER_ITEMS_REFUNDED.send(player, "%AMOUNT%;" + order.getVolume(),
                    "%ITEM%;" + book.getItem().getName());
        } else {
            // TODO: add coins to wallet
            user.save();
            MarketLang.ORDER_COINS_REFUNDED.send(player, "%COINS%" + (order.getVolume() * order.getPrice()));
        }
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
    }
    
    public void claimGoods(Order order) {
        // todo if there are no items to claim
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        User user = User.get(uuid);
        OrderBook book = OrderBook.get(order.getBookItem());
        if (order.getType() == Order.Type.BUY) {
            int items = 0;
            for (Trade t : order.getTrades()) {
                if (t.isClaimed()) continue;
                items += t.getAmount();
                t.claim();
                t.save();
            }
            
            Material material = book.getItem().getMaterial();
            for (int i = 0; i < items; i++) player.getInventory().addItem(new ItemStack(material));
            
            MarketLang.ORDER_ITEMS_CLAIMED.send(player, "%NUM%;" + items, "%AMOUNT%;" + order.getAmount(),
                    "%ITEM%;" + book.getItem().getName());
        } else {
            int coins = 0;
            for (Trade t : order.getTrades()) {
                if (t.isClaimed()) continue;
                coins += t.getAmount() * t.getPrice();
                t.claim();
                t.save();
            }
            
            // TODO: add coins to wallet
            user.save();
            
            MarketLang.ORDER_ITEMS_CLAIMED.send(player, "%COINS%" + coins, "%AMOUNT%;" + order.getAmount(),
                    "%ITEM%;" + book.getItem().getName());
        }
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
    }
    
    public void save() {
        marketHandler.getProfiles().put(uuid, this);
    }
    
    public void remove() {
        marketHandler.getProfiles().remove(uuid);
    }
    
    // ----------------------------------------------------------------------------------------------------
    // STATIC METHODS
    // ----------------------------------------------------------------------------------------------------
    
    public static boolean hasProfile(UUID uuid) {
        return marketHandler.getProfiles().containsKey(uuid);
    }
    
    public static OrderProfile get(UUID uuid) {
        if (!hasProfile(uuid)) return null;
        return marketHandler.getProfiles().get(uuid);
    }
}
