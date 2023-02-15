package net.pixlies.business.util;

import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Embargo;
import net.pixlies.business.market.Tariff;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.Nation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ListDisplayUtil {
      public static void displayList(int size, @Nullable Integer page, Player player, boolean tariff) {
            int pages = (int) Math.ceil(size / 7.0);
      
            // If the "page" argument is null
            if (page == null) {
                  if (tariff) displayTariffListPage(player, size, 1);
                  else displayEmbargoListPage(player, size, 1);
                  return;
            }
      
            // If the page does not exist
            if (!CommandPreconditions.doesPageExist(player, page, pages))
                  return;
      
            if (tariff) displayTariffListPage(player, size, page);
            else displayEmbargoListPage(player, size, page);
      }
      
      public static void displayEmbargoListPage(Player player, int size, int page) {
            // Each page will have 7 entries
            int pages = (int) Math.ceil(size / 7.0);
            int limit = Math.min(7 * page, size);
      
            MarketLang.EMBARGO_GLOBAL.send(player);
            for (int i = (page - 1) * 7 + 1; i <= limit; i++) {
                  Embargo embargo = Embargo.getAll().get(i - 1);
                  String from = Objects.requireNonNull(Nation.getFromId(embargo.initId())).getName();
                  String to = Objects.requireNonNull(Nation.getFromId(embargo.targetId())).getName();
                  MarketLang.EMBARGO_GLOBAL_FORMAT.send(player, "%NX%;" + from, "%NY%;" + to);
            }
            MarketLang.PAGE_INDEX.send(player, "%PAGE%;" + page, "%MAX%;" + pages);
      }
      
      public static void displayTariffListPage(Player player, int size, int page) {
            // Each page will have 7 entries
            int pages = (int) Math.ceil(size / 7.0);
            int limit = Math.min(7 * page, size);
      
            MarketLang.TARIFF_GLOBAL.send(player);
            for (int i = (page - 1) * 7 + 1; i <= limit; i++) {
                  Tariff tariff = Tariff.getAll().get(i - 1);
                  String from = Objects.requireNonNull(Nation.getFromId(tariff.getInitId())).getName();
                  String to = Objects.requireNonNull(Nation.getFromId(tariff.getTargetId())).getName();
                  MarketLang.TARIFF_GLOBAL_FORMAT.send(
                          player,
                          "%NX%;" + from,
                          "%NY%;" + to,
                          "%RATE%;" + tariff.getFormattedRate()
                  );
            }
            MarketLang.PAGE_INDEX.send(player, "%PAGE%;" + page, "%MAX%;" + pages);
      }
}
