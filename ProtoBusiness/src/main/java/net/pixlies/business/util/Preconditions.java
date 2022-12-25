package net.pixlies.business.util;

import net.pixlies.business.locale.MarketLang;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class Preconditions {
      public static boolean doesPlayerExist(Player player, String name) {
            if (Bukkit.getPlayerExact(name) == null) {
                  MarketLang.PLAYER_DOES_NOT_EXIST.send(player);
                  player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100F, 0.5F);
                  return false;
            }
            return true;
      }
}
