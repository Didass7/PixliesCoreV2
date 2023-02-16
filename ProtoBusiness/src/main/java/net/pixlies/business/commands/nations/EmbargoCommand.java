package net.pixlies.business.commands.nations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Embargo;
import net.pixlies.business.threads.EmbargoExpirationThread;
import net.pixlies.business.util.ListDisplayUtil;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.core.utils.TextUtils;
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
 * Embargo command.
 *
 * @author vyketype
 */
@CommandAlias("embargo")
@CommandPermission("pixlies.business.embargo")
public class EmbargoCommand extends BaseCommand {
      @Subcommand("listlocal")
      @Description("Retrieve the list of embargoes which concern your nation")
      public void onEmbargoLocal(Player player) {
            NationProfile profile = NationProfile.get(player.getUniqueId());
      
            // If the player is not in a nation
            if (!CommandPreconditions.isPlayerInNation(player, profile))
                  return;
      
            // Sort through the embargoes
            String nationsId = profile.getNationId();
            List<Embargo> incoming = new ArrayList<>();
            List<Embargo> outgoing = new ArrayList<>();
            for (Embargo embargo : Embargo.getAll()) {
                  if (Objects.equals(embargo.targetId(), nationsId))
                        incoming.add(embargo);
                  else if (Objects.equals(embargo.initId(), nationsId))
                        outgoing.add(embargo);
            }
      
            // If there are no embargoes
            if (CommandPreconditions.ifNoLocalEmbargoes(player, incoming, outgoing))
                  return;
      
            // Show incoming embargoes
            if (!incoming.isEmpty()) {
                  MarketLang.EMBARGO_LOCAL_INCOMING.send(player);
                  incoming.forEach(embargo -> {
                        String from = Objects.requireNonNull(Nation.getFromId(embargo.initId())).getName();
                        MarketLang.EMBARGO_LOCAL_FORMAT.send(player, "%NX%;" + from);
                  });
            }
      
            // Show outgoing embargoes
            if (!outgoing.isEmpty()) {
                  MarketLang.EMBARGO_LOCAL_OUTGOING.send(player);
                  outgoing.forEach(embargo -> {
                        String to = Objects.requireNonNull(Nation.getFromId(embargo.targetId())).getName();
                        MarketLang.EMBARGO_LOCAL_FORMAT.send(player, "%NX%;" + to);
                  });
            }
      }
      
      @Subcommand("listglobal")
      @Description("Retrieve the list of all embargoes")
      public void onEmbargoGlobal(Player player, @Optional Integer page) {
            // If there are no embargoes
            if (CommandPreconditions.ifNoGlobalEmbargoes(player))
                  return;
      
            int size = Embargo.getAll().size();
            ListDisplayUtil.displayList(size, page, player, false);
      }
      
      @Subcommand("add")
      @Syntax("<nation> [durationHours]")
      @Description("Embargo a nation")
      public void onEmbargoAdd(Player player, String strArgs) {
            String[] args = StringUtils.split(strArgs, " ", -1);
            NationProfile nationProfile = NationProfile.get(player.getUniqueId());
            
            if (!CommandPreconditions.embargoSet(player, args[0]))
                  return;
            
            String targetId = Objects.requireNonNull(Nation.getFromName(args[0])).getNationId();
            int duration = args.length > 1 ? CommandPreconditions.getValidDuration(player, args[1]) : -1;
            Embargo embargo = new Embargo(TextUtils.generateId(9), nationProfile.getNationId(), targetId, duration);
            embargo.save();
            
            String strDuration = duration == -1 ? "(no expiration)" : duration + "h";
            
            // Send message to the players of the initial nation
            for (UUID uuid : Objects.requireNonNull(nationProfile.getNation()).getMembers()) {
                  MarketLang.INCOMING_EMBARGO_ADDED.send(
                          Objects.requireNonNull(Bukkit.getPlayer(uuid)),
                          "%NATION%;" + args[0],
                          "%DURATION%;" + strDuration
                  );
            }
            
            // Send message to the players of the target nation
            for (UUID uuid : Objects.requireNonNull(Nation.getFromName(args[0])).getMembers()) {
                  MarketLang.OUTGOING_EMBARGO_ADDED.send(
                          Objects.requireNonNull(Bukkit.getPlayer(uuid)),
                          "%NATION%;" + nationProfile.getNation().getName(),
                          "%DURATION%;" + strDuration
                  );
            }
            
            if (duration != -1) {
                  EmbargoExpirationThread.EMBARGO_EXPIRATIONS.put(embargo.embargoId(), embargo.duration() * 60);
            }
      }
      
      @Subcommand("remove")
      @Syntax("<nation>")
      @Description("Remove an embargo from a nation")
      public void onEmbargoRemove(Player player, String targetNation) {
            NationProfile nationProfile = NationProfile.get(player.getUniqueId());
            
            if (!CommandPreconditions.embargoRemove(player, targetNation))
                  return;
            
            String embargoId = Embargo.getEmbargoId(nationProfile.getNation().getName(), targetNation);
            Embargo embargo = Embargo.get(embargoId);
            
            if (!CommandPreconditions.isEmbargoDeleteSuccessful(player, embargo))
                  return;
            
            embargo.sendDeletionMessages();
            EmbargoExpirationThread.EMBARGO_EXPIRATIONS.remove(embargoId);
      }
      
      @Default
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
}
