package net.pixlies.lobby.runnables.impl;

import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.pixlies.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class TimerRunnable extends BukkitRunnable {
      private static final Lobby instance = Lobby.getInstance();
      
      private String name;
      
      @Override
      public void run() {
            int remainingSeconds = instance.getTimerHandler().getRemainingSeconds();
            
            if (remainingSeconds == 0) {
                  if (instance.getTimerHandler().isBombing())
                        Bukkit.getOnlinePlayers().forEach(p -> p.banPlayer("Bombing achieved."));
                  
                  instance.getTimerHandler().finishTimer();
                  instance.getTimerHandler().setTaskId(0);
                  cancel();
                  return;
            }
            
            String message = "§b§l" + name + "§r§7: §a" + remainingSeconds + " seconds";
            
            Bukkit.getOnlinePlayers().forEach(p -> {
                  p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            });
            instance.getTimerHandler().decreaseSeconds();
      }
}