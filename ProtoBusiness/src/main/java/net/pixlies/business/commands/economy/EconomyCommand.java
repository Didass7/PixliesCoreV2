package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.CommandPreconditions;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("economy|eco")
public class EconomyCommand extends BaseCommand {
      @Subcommand("give")
      @CommandPermission("pixlies.business.economy.give")
      @Description("Give a player some money")
      @Syntax("<player> <amount>")
      public void onEconomyGive(Player player, String strArgs) {
            if (!CommandPreconditions.economy(player, strArgs))
                  return;
      
            String[] args = StringUtils.split(strArgs, " ", -1);
            String targetName = args[0];
            String strAmount = args[1];
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
            
            NationProfile nationProfile = NationProfile.get(offlineTarget.getUniqueId());
            nationProfile.addBalance(Double.parseDouble(strAmount));
            nationProfile.save();
      
            MarketLang.BALANCE_ADDED_SENDER.send(player, "%COINS%;" + strAmount, "%PLAYER%;" + targetName);
            
            if (offlineTarget.isOnline()) {
                  Player target = offlineTarget.getPlayer();
                  MarketLang.BALANCE_ADDED_SENDER.send(target, "%COINS%;" + strAmount, "%PLAYER%;" + player.getName());
                  SoundUtil.success(target);
            }
      }
      
      @Subcommand("remove")
      @CommandPermission("pixlies.business.economy.remove")
      @Description("Remove some money from a player's bank account")
      @Syntax("<player> <amount>")
      public void onEconomyRemove(Player player, String strArgs) {
            if (!CommandPreconditions.economy(player, strArgs))
                  return;
      
            String[] args = StringUtils.split(strArgs, " ", -1);
            String targetName = args[0];
            String strAmount = args[1];
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
            
            NationProfile nationProfile = NationProfile.get(offlineTarget.getUniqueId());
            nationProfile.removeBalance(Double.parseDouble(strAmount));
            nationProfile.save();
            
            MarketLang.BALANCE_REMOVED_SENDER.send(player, "%COINS%;" + strAmount, "%PLAYER%;" + targetName);
            
            if (offlineTarget.isOnline()) {
                  Player target = offlineTarget.getPlayer();
                  MarketLang.BALANCE_REMOVED_TARGET.send(target, "%COINS%;" + strAmount, "%PLAYER%;" + player.getName());
                  SoundUtil.grandError(target);
            }
      }
      
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
}
