package net.pixlies.business.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Վե՛հ գաղափար Դաշնակցութեան։
 */
public class SoundUtil {
      public static void success(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 0.5F);
      }
      
      public static void error(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100F, 0.5F);
      }
      
      public static void grandSuccess(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100F, 0.5F);
      }
      
      public static void grandError(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 0.5F);
      }
      
      public static void littleSuccess(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 2F);
      }
      
      public static void challengeComplete(Player player) {
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 50F, 1F);
      }
      
      public static void cancelledOrder(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_NETHERITE_BLOCK_PLACE, 100F, 1F);
      }
}
