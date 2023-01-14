package net.pixlies.business.commands.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("convertgold")
@CommandPermission("pixlies.business.convertgold")
@Description("Convert some gold ingots into coins")
public class ConvertGoldCommand extends BaseCommand {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Default
      @Syntax("<goldIngots>")
      public void onConvertItems(Player player, int amount) {
            // If the player does not have enough gold ingots
            if (!CommandPreconditions.playerHasEnoughItems(player, OrderItem.GOLD_INGOT, amount))
                  return;
            
            int conversionRate = instance.getConfig().getInt("goldStandard");
            double coins = amount * conversionRate;
            
            // Give coins
            NationProfile profile = NationProfile.get(player.getUniqueId());
            profile.addBalance(coins);
            profile.save();
            
            // Take gold ingots from inventory
            player.getInventory().removeItemAnySlot(new ItemStack(Material.GOLD_INGOT, amount));
            player.updateInventory();
            
            // Send message, play sound
            MarketLang.CONVERTED_GOLD_TO_COINS.send(player, "%GOLD%;" + amount, "%COINS%;" + coins);
            SoundUtil.littleSuccess(player);
      }
}
