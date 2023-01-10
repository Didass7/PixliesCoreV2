package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("balance|bal")
@CommandPermission("pixlies.business.balance")
@Description("Check your or another player's balance")
public class BalanceCommand extends BaseCommand {
      @Default
      public void onBalance(Player player, @Optional String targetName) {
            if (targetName == null || targetName.isEmpty()) {
                  NationProfile profile = NationProfile.get(player.getUniqueId());
                  MarketLang.BALANCE.send(player, "%PLAYER%;" + player.getName(), "%BALANCE%;" + profile.getBalance());
                  return;
            }
      
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
      
            // If the player has not joined before
            if (!CommandPreconditions.hasPlayerEverJoined(player, offlineTarget.getUniqueId()))
                  return;
      
            NationProfile profile = NationProfile.get(offlineTarget.getUniqueId());
            MarketLang.BALANCE.send(player, "%PLAYER%;" + offlineTarget.getName(), "%BALANCE%;" + profile.getBalance());
      }
}
