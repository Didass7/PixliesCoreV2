package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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
            
            // Add items
            Map<Integer, ItemStack> map = player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, gold));
            player.updateInventory();
            
            // Drop items which were unable to be added
            for (Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
                  World world = player.getWorld();
                  Location location = player.getLocation();
                  world.dropItemNaturally(location, entry.getValue());
            }
            
            // Take coins from wallet
            NationProfile profile = NationProfile.get(player.getUniqueId());
            profile.removeBalance(coins);
            profile.save();
      
            // Send message, play sound
            MarketLang.CONVERTED_COINS_TO_GOLD.send(player, "%COINS%;" + coins, "%GOLD%;" + gold);
            SoundUtil.littleSuccess(player);
      }
}
