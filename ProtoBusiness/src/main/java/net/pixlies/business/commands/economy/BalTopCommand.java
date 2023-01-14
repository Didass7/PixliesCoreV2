package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.threads.BalTopThread;
import net.pixlies.nations.nations.Nation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@CommandAlias("balancetop|baltop")
@CommandPermission("pixlies.business.balancetop")
@Description("Check the top 10 players' (or nations') balance")
public class BalTopCommand extends BaseCommand {
      @Default
      @Syntax("[nations]")
      public void onBalanceTop(CommandSender sender, @Optional String nations) {
            PrettyTime prettyTime = new PrettyTime();
            Set<Map.Entry<String, Double>> entries;
      
            if (nations != null && nations.equalsIgnoreCase("nations")) {
                  MarketLang.BALTOP_NATIONS_HEADER.send(sender);
                  MarketLang.BALTOP_LAST_UPDATED.send(
                          sender,
                          "%TIME%;" + prettyTime.format(BalTopThread.BALTOP_NATIONS_DATE)
                  );
                  entries = BalTopThread.BALTOP_NATIONS_MAP.entrySet();
            } else {
                  MarketLang.BALTOP_HEADER.send(sender);
                  MarketLang.BALTOP_LAST_UPDATED.send(
                          sender,
                          "%TIME%;" + prettyTime.format(BalTopThread.BALTOP_PLAYERS_DATE)
                  );
                  entries = BalTopThread.BALTOP_PLAYERS_MAP.entrySet();
            }
      
            int i = 1;
            for (Map.Entry<String, Double> entry : entries) {
                  String name;
                  if (nations.equalsIgnoreCase("nations")) {
                        name = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())).getName();
                  } else {
                        name = Objects.requireNonNull(Nation.getFromId(entry.getKey())).getName();
                  }
                  
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
                          "%NAME%;" + name,
                          "%BALANCE%;" + Math.round(entry.getValue())
                  );
                  i++;
            }
      }
}
