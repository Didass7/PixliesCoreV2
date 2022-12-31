package net.pixlies.lobby.handlers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TimerHandler {
      @Getter
      @Setter
      private int taskId = 0;
      
      @Getter
      @Setter
      private int remainingSeconds = 0;
      
      public void decreaseSeconds() {
            remainingSeconds--;
      }
      
      public void finishTimer() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                  p.sendTitle("§c§lTime's up!", "§7Candidates, stop talking!");
                  p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100F, 0.5F);
            }
      }
}
