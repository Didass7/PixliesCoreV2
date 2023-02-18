package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("economy|eco|bank")
public class BankCommand extends BaseCommand {
      @Subcommand("clear")
      @CommandPermission("pixlies.business.bank.clear")
      @Description("Clear a player's bank account")
      @Syntax("<player>")
      public void onBankClear(Player player, String targetName) {
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
      
            // If the player has not joined before
            if (!CommandPreconditions.hasPlayerEverJoined(player, offlineTarget.getUniqueId()))
                  return;
      
            NationProfile nationProfile = NationProfile.get(offlineTarget.getUniqueId());
            nationProfile.removeBalance(nationProfile.getBalance());
            nationProfile.save();
      
            MarketLang.BALANCE_CLEARED_SENDER.send(player, "%PLAYER%;" + targetName);
      
            if (offlineTarget.isOnline()) {
                  Player target = offlineTarget.getPlayer();
                  MarketLang.BALANCE_CLEARED_TARGET.send(target);
                  SoundUtil.grandError(target);
            }
      }
      
      @Subcommand("deposit|add|give")
      @CommandPermission("pixlies.business.bank.give")
      @Description("Give a player some money")
      @Syntax("<player> <amount>")
      public void onBankDeposit(Player player, String strArgs) {
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
                  MarketLang.BALANCE_ADDED_TARGET.send(target, "%COINS%;" + strAmount);
                  SoundUtil.success(target);
            }
      }
      
      @Subcommand("withdraw|remove|take")
      @CommandPermission("pixlies.business.bank.remove")
      @Description("Remove some money from a player's bank account")
      @Syntax("<player> <amount>")
      public void onBankWithdraw(Player player, String strArgs) {
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
                  MarketLang.BALANCE_REMOVED_TARGET.send(target, "%COINS%;" + strAmount);
                  SoundUtil.grandError(target);
            }
      }
      
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
}
