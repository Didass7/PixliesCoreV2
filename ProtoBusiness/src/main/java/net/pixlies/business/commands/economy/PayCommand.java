package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

@CommandAlias("pay")
@CommandPermission("pixlies.business.pay")
@Description("Pay another player some money")
public class PayCommand extends BaseCommand {
      @CommandCompletion("@players")
      @Syntax("<player> <amount>")
      @Default
      public void onPay(Player player, String strArgs) {
            if (!CommandPreconditions.economy(player, strArgs))
                  return;
      
            String[] args = StringUtils.split(strArgs, " ", -1);
            String targetName = args[0];
            String strAmount = args[1];
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
            
            double amount = Double.parseDouble(strAmount);
      
            if (!CommandPreconditions.playerHasEnoughMoney(player, amount))
                  return;
      
            // Add money to target
            NationProfile targetProfile = NationProfile.get(offlineTarget.getUniqueId());
            targetProfile.addBalance(BigDecimal.valueOf(amount));
            targetProfile.save();
            
            // Remove money from target
            NationProfile playerProfile = NationProfile.get(player.getUniqueId());
            playerProfile.removeBalance(BigDecimal.valueOf(amount));
            playerProfile.save();
      
            MarketLang.PAID_SENDER.send(player, "%COINS%;" + strAmount, "%PLAYER%;" + targetName);
      
            if (offlineTarget.isOnline()) {
                  Player target = offlineTarget.getPlayer();
                  MarketLang.PAID_TARGET.send(target, "%COINS%;" + strAmount, "%PLAYER%;" + player.getName());
                  SoundUtil.success(target);
            }
      }
}
