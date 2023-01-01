package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("bomb")
@CommandPermission("pixlies.lobby.bomb")
public class BombCommand extends BaseCommand {
      public void onBomb(CommandSender sender, String playerName) {
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null || !player.isOnline()) {
                  Lang.PLAYER_DOESNT_EXIST.send(sender);
                  return;
            }
            player.banPlayer("You were bombed. We cannot do anything to help you, as you were bombed.");
      }
}
