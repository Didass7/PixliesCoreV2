package net.pixlies.business.commands.market;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.util.Preconditions;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Price command.
 *
 * @author vyketype
 */
@CommandAlias("price|prix")
@CommandPermission("pixlies.business.price")
public class PriceCommand extends BaseCommand {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Default
      @Description("Retrieve the best prices of the held item")
      public void onPrice(Player player) {
            Material mat = player.getInventory().getItemInMainHand().getType();
            
            // If the material is air
            if (Preconditions.isPlayerHoldingAir(player, mat))
                  return;
            
            OrderItem item = OrderItem.getFromMaterial(mat);
            
            // If the item is not on the market
            if (!Preconditions.doesMarketItemExist(player, item))
                  return;
      
            assert item != null;
            OrderBook book = OrderBook.get(item);
            
            // Send price summaries
            MarketLang.PRICE_SUMMARY.send(player, "%ITEM%;" + item.getName());
            MarketLang.PRICE_BEST_BUY.send(
                    player,
                    "%PRICE%;" + book.getLowestBuyPrice(player.getUniqueId()),
                    "%AMOUNT%;" + book.getBuyOrders().size()
            );
            MarketLang.PRICE_BEST_SELL.send(
                    player,
                    "%PRICE%;" + book.getHighestSellPrice(player.getUniqueId()),
                    "%AMOUNT%;" + book.getSellOrders().size()
            );
      }
}
