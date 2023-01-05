package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.threads.BalTopThread;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Map;
import java.util.UUID;

@CommandAlias("balancetop|baltop")
@CommandPermission("pixlies.business.balancetop")
@Description("Check the top 10 players' balance")
public class BalTopCommand extends BaseCommand {
      @Default
      public void onBalanceTop(CommandSender sender) {
            MarketLang.BALTOP_HEADER.send(sender);
            
            PrettyTime prettyTime = new PrettyTime();
            MarketLang.BALTOP_LAST_UPDATED.send(sender, "%TIME%;" + prettyTime.format(BalTopThread.DATE));
      
            int i = 1;
            for (Map.Entry<UUID, Double> entry : BalTopThread.BALTOP_MAP.entrySet()) {
                  OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
                  String numberColor;
                  switch (i) {
                        case 1 -> numberColor = "6";
                        case 2 -> numberColor = "7";
                        case 3 -> numberColor = "c";
                        default -> numberColor = "f";
                  }
                  MarketLang.BALTOP_FORMAT.send(
                          sender,
                          "%COLOR%;" + numberColor,
                          "%NUMBER%;" + i,
                          "%NAME%;" + offlinePlayer.getName(),
                          "%BALANCE%;" + Math.round(entry.getValue())
                  );
                  i++;
            }
      }
}
