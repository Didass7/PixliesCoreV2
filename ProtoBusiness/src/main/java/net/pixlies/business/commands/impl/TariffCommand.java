package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.Tariff;
import net.pixlies.business.util.Preconditions;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.ranks.NationPermission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Tariff command.
 *
 * @author vyketype
 */
@CommandAlias("tariff|tariffs|trf")
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
            for (Tariff t : instance.getMarketManager().getTariffs().values()) {
                  if (Objects.equals(t.getFrom(), nationsId))
                        incoming.add(t);
                  else if (Objects.equals(t.getTo(), nationsId))
                        outgoing.add(t);
            }
            
            // If there are no tariffs
            if (Preconditions.ifNoLocalTariffs(player, incoming, outgoing))
                  return;
            
            // Show incoming tariffs
            if (!incoming.isEmpty()) {
                  MarketLang.TARIFF_LOCAL_INCOMING.send(player);
                  incoming.forEach(t -> {
                        String from = Objects.requireNonNull(Nation.getFromId(t.getFrom())).getName();
                        MarketLang.TARIFF_INCOMING_FORMAT.send(player, "%NATION%;" + from,
                                "%RATE%;" + t.getFormattedRate());
                  });
            }
            
            // Show outgoing tariffs
            if (!outgoing.isEmpty()) {
                  MarketLang.TARIFF_LOCAL_OUTGOING.send(player);
                  outgoing.forEach(t -> {
                        String to = Objects.requireNonNull(Nation.getFromId(t.getTo())).getName();
                        MarketLang.TARIFF_OUTGOING_FORMAT.send(player, "%NATION%;" + to,
                                "%RATE%;" + t.getFormattedRate());
                  });
            }
      }
      
      @Subcommand("global")
      @Description("Retrieve the list of all incoming and outgoing tariffs")
      public void onTariffGlobal(Player player) {
            // If there are no tariffs
            if (Preconditions.ifNoGlobalTariffs(player))
                  return;
            
            // Show all tariffs
            MarketLang.TARIFF_GLOBAL.send(player);
            for (Tariff t : instance.getMarketManager().getTariffs().values()) {
                  MarketLang.TARIFF_GLOBAL_FORMAT.send(
                          player,
                          "%FROM%;" + Objects.requireNonNull(Nation.getFromId(t.getTo())).getName(),
                          "%TO%;" + Objects.requireNonNull(Nation.getFromId(t.getTo())).getName(),
                          "%RATE%;" + t.getFormattedRate()
                  );
            }
      }
      
      @Subcommand("set")
      @Description("Set a tariff for a nation")
      public void onTariffSet(Player player, String to, double rate) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(player.getUniqueId());
            Nation from = Nation.getFromName(to);
            double maxRate = instance.getConfig().getDouble("tariffMaxRate");
            
            boolean notInNation = !profile.isInNation();
            boolean noPermission = !NationPermission.MANAGE_TARIFFS.hasPermission(player) && !user.isBypassing();
            boolean nationNull = from == null;
            boolean rateNotValid = rate > maxRate || rate < 0.01;
            
            if (notInNation) NationsLang.NOT_IN_NATION.send(player);
            if (noPermission) NationsLang.NATION_NO_PERMISSION.send(player);
            if (nationNull) NationsLang.NATION_DOES_NOT_EXIST.send(player);
            if (rateNotValid) MarketLang.TARIFF_RATE_NOT_VALID.send(player, "%MAX%;" + maxRate);
            
            if (notInNation || noPermission || nationNull || rateNotValid) {
                  player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                  return;
            }
            
            String fromId = profile.getNationId();
            String toId = Objects.requireNonNull(Nation.getFromName(to)).getNationId();
            Tariff tariff = new Tariff(fromId, toId, rate);
            
            instance.getMarketManager().getTariffs().put(tariff.getTariffId(), tariff);
            for (UUID uuid : from.getMembers()) {
                  MarketLang.INCOMING_TARIFF_SET.send(Bukkit.getPlayer(uuid), "%NATION%;" + to, "%RATE%;" + rate);
            }
            for (UUID uuid : Objects.requireNonNull(Nation.getFromName(to)).getMembers()) {
                  MarketLang.OUTGOING_TARIFF_SET.send(Bukkit.getPlayer(uuid), "%NATION%;" + from.getName(),
                          "%RATE%;" + rate);
            }
      }
      
      @Subcommand("remove")
      @Description("Remove a tariff from a nation")
      public void onTariffRemove(Player player, String to) {
            User user = User.get(player.getUniqueId());
            NationProfile profile = NationProfile.get(player.getUniqueId());
            String from = profile.getNation().getName();
            
            boolean notInNation = !profile.isInNation();
            boolean noPermission = !NationPermission.MANAGE_TARIFFS.hasPermission(player) && !user.isBypassing();
            boolean tariffNull = instance.getMarketManager().getTariffId(from, to) == null;
            
            if (notInNation) NationsLang.NOT_IN_NATION.send(player);
            if (noPermission) NationsLang.NATION_NO_PERMISSION.send(player);
            if (tariffNull) MarketLang.TARIFF_DOES_NOT_EXIST.send(player);
            
            if (notInNation || noPermission || tariffNull) {
                  player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                  return;
            }
            
            instance.getMarketManager().getTariffs().remove(instance.getMarketManager().getTariffId(from, to));
            for (UUID uuid : Objects.requireNonNull(Nation.getFromName(from)).getMembers()) {
                  MarketLang.INCOMING_TARIFF_REMOVED.send(Bukkit.getPlayer(uuid), "%NATION%;" + to);
            }
            for (UUID uuid : Objects.requireNonNull(Nation.getFromName(to)).getMembers()) {
                  MarketLang.OUTGOING_TARIFF_REMOVED.send(Bukkit.getPlayer(uuid), "%NATION%;" + from);
            }
      }
      
      @Default
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
}
