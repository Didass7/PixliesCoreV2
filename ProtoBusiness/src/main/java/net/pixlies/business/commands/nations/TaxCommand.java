package net.pixlies.business.commands.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

@CommandAlias("tax|nationtax")
public class TaxCommand extends BaseCommand {
      @Subcommand("query")
      @CommandPermission("pixlies.business.tax.query")
      @Description("Check the tax rate for your nation")
      public void onTaxQuery(Player player) {
            NationProfile profile = NationProfile.get(player.getUniqueId());
            
            // If the player is not in a nation
            if (!CommandPreconditions.isPlayerInNation(player, profile))
                  return;
      
            BigDecimal rate = profile.getNation().getTaxRate().multiply(BigDecimal.valueOf(100));
            if (rate.equals(BigDecimal.valueOf(0))) {
                  MarketLang.TAX_RATE_ZERO.send(player);
            } else {
                  MarketLang.TAX_RATE_QUERY.send(player, "%RATE%;" + rate.doubleValue());
            }
      }
      
      @Subcommand("set")
      @CommandPermission("pixlies.business.tax.set")
      @Description("Set a new tax rate for your nation")
      @Syntax("<rateDecimal>")
      public void onTaxSet(Player player, String strRateDecimal) {
            NationProfile profile = NationProfile.get(player.getUniqueId());
      
            // Preconditions for /tax set
            if (!CommandPreconditions.taxSet(player, profile, strRateDecimal))
                  return;
            
            BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(strRateDecimal));
            
            Nation nation = profile.getNation();
            nation.setTaxRate(rate);
            nation.save();
            
            for (Player citizen : nation.getOnlineMembersAsPlayer()) {
                  if (rate.equals(BigDecimal.valueOf(0))) {
                        MarketLang.CLEARED_TAX_RATE.send(citizen);
                  } else {
                        MarketLang.NEW_TAX_RATE_SET.send(citizen, "%RATE%;" + rate.multiply(BigDecimal.valueOf(100)));
                  }
            }
      }
      
      @Default
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
}
