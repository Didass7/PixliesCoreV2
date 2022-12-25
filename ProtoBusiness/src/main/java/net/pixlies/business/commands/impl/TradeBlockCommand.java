package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.util.Preconditions;
import net.pixlies.core.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * TradeBlock command.
 *
 * @author vyketype
 */
@CommandAlias("tradeblock")
@CommandPermission("pixlies.business.tradeblock")
public class TradeBlockCommand extends BaseCommand {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Subcommand("list")
      @Description("View who you have blocked")
      public void onTradeBlockList(Player player) {
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            List<UUID> list = profile.getBlockedPlayers();
      
            // If there are no trade-blocked players
            if (list.isEmpty()) {
                  MarketLang.TRADE_BLOCK_LIST_EMPTY.send(player);
                  return;
            }
            
            // Format names
            StringBuilder names = new StringBuilder();
            for (UUID uuid : list) {
                  String name = Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName();
                  names.append(Rank.getRank(uuid).getColor()).append(name);
                  if (list.indexOf(uuid) != list.size() - 1) {
                        names.append(ChatColor.translateAlternateColorCodes('§', ","));
                  }
            }
            
            // Send message
            MarketLang.TRADE_BLOCK_LIST.send(player, "%LIST%;" + names);
      }
      
      @Subcommand("add")
      @Description("Block a player from trading with you")
      public void onTradeBlockAdd(Player player, String name) {
            // If the player does not exist
            if (!Preconditions.doesPlayerExist(player, name))
                  return;
            
            // Block player
            Player target = Bukkit.getPlayerExact(name);
            assert target != null;
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            profile.tradeBlockPlayer(target.getUniqueId());
            profile.save();
            
            // Send messages and play sound
            MarketLang.TRADE_BLOCK_ADDED.send(
                    player,
                    "%TARGET%;" + Rank.getRank(target.getUniqueId()).getColor() + name
            );
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 0.5F);
      }
      
      @Subcommand("remove")
      @Description("Unblock a player from trading with you")
      public void onTradeBlockRemove(Player player, String name) {
            // If the player does not exist
            if (!Preconditions.doesPlayerExist(player, name))
                  return;
            
            // Unblock player
            Player target = Bukkit.getPlayerExact(name);
            assert target != null;
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            profile.unTradeBlockPlayer(target.getUniqueId());
            profile.save();
            
            // Send messages and play sound
            MarketLang.TRADE_BLOCK_REMOVED.send(
                    player,
                    "%TARGET%;" + Rank.getRank(target.getUniqueId()).getColor() + name
            );
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 2F);
      }
      
      @Default
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
}
