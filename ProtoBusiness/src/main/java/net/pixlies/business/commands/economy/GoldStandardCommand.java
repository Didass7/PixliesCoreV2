package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.ProtoBusinesss;
import net.pixlies.business.locale.MarketLang;
import org.bukkit.command.CommandSender;

@CommandAlias("goldstandard|gs")
@CommandPermission("pixlies.business.goldstandard")
@Description("See the current gold conversion rates")
public class GoldStandardCommand extends BaseCommand {
      private static final ProtoBusinesss instance = ProtoBusinesss.getInstance();
      
      @Default
      public void onGoldStandard(CommandSender sender) {
            int rate = instance.getConfig().getInt("goldStandard");
            MarketLang.GOLD_STANDARD.send(sender, "%RATE%;" + rate);
      }
}
