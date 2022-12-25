package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import org.bukkit.Material;
import org.bukkit.Sound;
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
            if (mat == Material.AIR) {
                  MarketLang.PRICE_NOT_HOLDING_AN_ITEM.send(player);
                  player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 1F);
                  return;
            }
            
            OrderItem item = OrderItem.getFromMaterial(mat);
            
            // If the item is not on the market
            if (item == null) {
                  MarketLang.ITEM_NOT_ON_MARKET.send(player);
                  player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 1F);
                  return;
            }
    
            OrderBook book = instance.getMarketManager().getBook(item);
            
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
