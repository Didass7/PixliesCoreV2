package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("convertcoins")
@CommandPermission("pixlies.business.convertcoins")
@Description("Convert some coins into gold ingots")
public class ConvertCoinsCommand extends BaseCommand {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Default
      public void onConvertCoins(Player player, double amount) {
            // If the player does not have enough money
            if (!CommandPreconditions.playerHasEnoughMoney(player, amount))
                  return;
            
            int conversionRate = instance.getConfig().getInt("goldStandard");
            double remainder = amount % conversionRate;
            double coins = amount - remainder;
            int gold = (int) Math.floor(coins / conversionRate);
            
            // Add items
            player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, gold));
            player.updateInventory();
            
            // Take coins from wallet
            NationProfile profile = NationProfile.get(player.getUniqueId());
            profile.removeBalance(coins);
            profile.save();
      
            // Send message, play sound
            MarketLang.CONVERTED_COINS_TO_GOLD.send(player, "%COINS%;" + coins, "%GOLD%;" + gold);
            SoundUtil.littleSuccess(player);
      }
}
