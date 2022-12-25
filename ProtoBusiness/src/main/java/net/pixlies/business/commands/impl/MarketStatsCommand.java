package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketProfile;
import org.bukkit.entity.Player;

/**
 * Market stats command.
 *
 * @author vyketype
 */
@CommandAlias("marketstats")
@CommandPermission("pixlies.business.marketstats")
@Description("Check your market statistics")
public class MarketStatsCommand extends BaseCommand {
      @Default
      public void onMarketStats(Player player) {
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            MarketLang.MARKET_STATS.send(player);
            MarketLang.MARKET_BUYS.send(player, "%X%;" + profile.getBuyOrdersMade());
            MarketLang.MARKET_SELLS.send(player, "%X%;" + profile.getSellOrdersMade());
            MarketLang.MARKET_TRADES.send(player, "%X%;" + profile.getTradesMade());
            MarketLang.MARKET_MONEY_SPENT.send(player, "%X%;" + profile.getMoneySpent());
            MarketLang.MARKET_MONEY_GAINED.send(player, "%X%;" + profile.getMoneyGained());
            MarketLang.MARKET_SOLD.send(player, "%X%;" + profile.getItemsSold());
            MarketLang.MARKET_BOUGHT.send(player, "%X%;" + profile.getItemsBought());
      }
}
