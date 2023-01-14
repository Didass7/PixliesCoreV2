package net.pixlies.business.commands.market;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import org.bukkit.command.CommandSender;

@CommandAlias("goldstandard|gs")
@CommandPermission("pixlies.business.goldstandard")
public class GoldStandardCommand extends BaseCommand {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Default
      @Description("See the current gold conversion rates")
      public void onGoldStandard(CommandSender sender) {
            double rate = instance.getConfig().getDouble("goldStandard");
            MarketLang.GOLD_STANDARD.send(sender, "%RATE%;" + rate);
      }
}
