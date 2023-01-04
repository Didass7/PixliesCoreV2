package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.nations.Tariff;
import net.pixlies.business.util.Preconditions;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Tariff command.
 * Ես իմ անուշ Հայաստանի արեւահամ բարն եմ սիրում։
 *
 * @author vyketype
 */
@CommandAlias("tariff|tariffs")
@CommandPermission("pixlies.business.tariff")
public class TariffCommand extends BaseCommand {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Subcommand("local")
      @Description("Retrieve the list of incoming and outgoing tariffs for your nation")
      public void onTariffLocal(Player player) {
            NationProfile profile = NationProfile.get(player.getUniqueId());
            
            // If the player is not in a nation
            if (!Preconditions.isPlayerInNation(player, profile))
                  return;
            
            // Sort through the tariffs
            String nationsId = profile.getNationId();
            List<Tariff> incoming = new ArrayList<>();
            List<Tariff> outgoing = new ArrayList<>();
            for (Tariff t : Tariff.getAll()) {
                  if (Objects.equals(t.getTargetId(), nationsId))
                        incoming.add(t);
                  else if (Objects.equals(t.getInitId(), nationsId))
                        outgoing.add(t);
            }
            
            // If there are no tariffs
            if (Preconditions.ifNoLocalTariffs(player, incoming, outgoing))
                  return;
            
            // Show incoming tariffs
            if (!incoming.isEmpty()) {
                  MarketLang.TARIFF_LOCAL_INCOMING.send(player);
                  incoming.forEach(t -> {
                        String from = Objects.requireNonNull(Nation.getFromId(t.getInitId())).getName();
                        MarketLang.TARIFF_LOCAL_FORMAT.send(player, "%NX%;" + from, "%RATE%;" + t.getFormattedRate());
                  });
            }
            
            // Show outgoing tariffs
            if (!outgoing.isEmpty()) {
                  MarketLang.TARIFF_LOCAL_OUTGOING.send(player);
                  outgoing.forEach(t -> {
                        String to = Objects.requireNonNull(Nation.getFromId(t.getTargetId())).getName();
                        MarketLang.TARIFF_LOCAL_FORMAT.send(player, "%NX%;" + to, "%RATE%;" + t.getFormattedRate());
                  });
            }
      }
      
      @Subcommand("global")
      @Description("Retrieve the list of all incoming and outgoing tariffs")
      public void onTariffGlobal(Player player, @Optional Integer page) {
            // If there are no tariffs
            if (Preconditions.ifNoGlobalTariffs(player))
                  return;
      
            int size = Tariff.getAll().size();
            int pages = (int) Math.ceil(size / 7.0);
            
            // If the "page" argument is null
            if (page == null) {
                  showGlobalListPage(player, 1);
                  return;
            }
      
            // If the page does not exist
            if (!Preconditions.doesPageExist(player, page, pages))
                  return;
      
            showGlobalListPage(player, page);
      }
      
      @Subcommand("set")
      @Description("Set a tariff for a nation")
      @Syntax("<nation> <decimalRate>")
      public void onTariffSet(Player player, String strArgs) {
            String[] args = StringUtils.split(strArgs, " ", -1);
            NationProfile nationProfile = NationProfile.get(player.getUniqueId());
            
            // If the rate argument is a valid number
            if (!Preconditions.isRateANumber(player, args[1]))
                  return;
            
            // If all the other preconditions are met
            if (!Preconditions.tariffSet(player, args[0], args[1]))
                  return;
            
            String targetNation = args[0];
            double rate = Double.parseDouble(args[1]);
            
            String initId = nationProfile.getNationId();
            String targetId = Objects.requireNonNull(Nation.getFromName(targetNation)).getNationId();
            Tariff tariff = new Tariff(initId, targetId, rate);
            tariff.save();
            
            // Send message to the players of the initial nation
            for (UUID uuid : Objects.requireNonNull(Nation.getFromId(initId)).getMembers()) {
                  MarketLang.INCOMING_TARIFF_SET.send(
                          Objects.requireNonNull(Bukkit.getPlayer(uuid)),
                          "%NATION%;" + targetNation,
                          "%RATE%;" + rate
                  );
            }
      
            // Send message to the players of the target nation
            for (UUID uuid : Objects.requireNonNull(Nation.getFromId(targetId)).getMembers()) {
                  MarketLang.OUTGOING_TARIFF_SET.send(
                          Objects.requireNonNull(Bukkit.getPlayer(uuid)),
                          "%NATION%;" + nationProfile.getNation().getName(),
                          "%RATE%;" + rate
                  );
            }
      }
      
      @Subcommand("remove")
      @Description("Remove a tariff from a nation")
      @Syntax("<nation>")
      public void onTariffRemove(Player player, String targetNation) {
            NationProfile nationProfile = NationProfile.get(player.getUniqueId());
      
            // If all the other preconditions are met
            if (!Preconditions.tariffRemove(player, targetNation))
                  return;
      
            String initId = nationProfile.getNationId();
            String targetId = Objects.requireNonNull(Nation.getFromName(targetNation)).getNationId();
            
            String tariffId = Tariff.getTariffId(nationProfile.getNation().getName(), targetNation);
            Tariff tariff = Tariff.get(tariffId);
            
            // If the program successfully removed the tariff
            if (!Preconditions.isTariffDeleteSuccessful(player, tariff))
                  return;
            
            // Send message to the players of the initial nation
            for (UUID uuid : Objects.requireNonNull(Nation.getFromId(initId)).getMembers()) {
                  if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline())
                        continue;
                  MarketLang.INCOMING_TARIFF_REMOVED.send(Bukkit.getPlayer(uuid), "%NATION%;" + targetNation);
            }
      
            // Send message to the players of the target nation
            for (UUID uuid : Objects.requireNonNull(Nation.getFromId(targetId)).getMembers()) {
                  if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline())
                        continue;
                  MarketLang.OUTGOING_TARIFF_REMOVED.send(
                          Bukkit.getPlayer(uuid),
                          "%NATION%;" + nationProfile.getNation().getName()
                  );
            }
      }
      
      @Default
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
      
      private void showGlobalListPage(Player player, int page) {
            // Each page will have 7 entries
            int size = Tariff.getAll().size();
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
