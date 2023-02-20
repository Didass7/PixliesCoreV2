package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.InventoryUtil;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

@CommandAlias("convertcoins|cc")
@CommandPermission("pixlies.business.convertcoins")
@Description("Convert some coins into gold ingots")
public class ConvertCoinsCommand extends BaseCommand {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Default
      @Syntax("<coins>")
      public void onConvertCoins(Player player, double amount) {
            // If the player does not have enough money
            if (!CommandPreconditions.playerHasEnoughMoney(player, amount))
                  return;
            
            int conversionRate = instance.getConfig().getInt("goldStandard");
            double remainder = amount % conversionRate;
            double coins = amount - remainder;
            int gold = (int) Math.floor(coins / conversionRate);
            
            // Add items to inventory
            InventoryUtil.addItemsToInventory(player, new ItemStack(Material.GOLD_INGOT, gold));
            
            // Take coins from wallet
            NationProfile profile = NationProfile.get(player.getUniqueId());
            profile.removeBalance(BigDecimal.valueOf(coins));
            profile.save();
      
            // Send message, play sound
            MarketLang.CONVERTED_COINS_TO_GOLD.send(player, "%COINS%;" + coins, "%GOLD%;" + gold);
            SoundUtil.littleSuccess(player);
      }
}
