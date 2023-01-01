package net.pixlies.lobby.handlers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
@Setter
public class TimerHandler {
      private int taskId = 0;
      private int remainingSeconds = 0;
      private boolean bombing = false;
      
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
