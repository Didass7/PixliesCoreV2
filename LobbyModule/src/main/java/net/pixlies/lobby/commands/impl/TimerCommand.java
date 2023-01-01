package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.runnables.impl.TimerRunnable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@CommandAlias("timer")
@CommandPermission("pixlies.debates.timer")
@Description("Set a timer as an ActionBar")
public class TimerCommand extends BaseCommand {
      private static final Lobby instance = Lobby.getInstance();
      
      @Default
      @HelpCommand
      public void onTimer(CommandHelp commandHelp) {
            commandHelp.showHelp();
      }
      
      @Subcommand("set")
      public void onTimer(Player player, String strArgs) {
            String[] args = StringUtils.split(strArgs, " ", -1);
            String name = strArgs.substring(0, strArgs.lastIndexOf(" "));
            String strTime = args[args.length - 1];
            
            try {
                  Integer.parseInt(strTime);
            } catch (NumberFormatException e) {
                  player.sendMessage(Lang.PIXLIES + "§cThis is not a valid number!");
                  player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100F, 0.5F);
                  return;
            }
            instance.getTimerHandler().setRemainingSeconds(Integer.parseInt(strTime));
            
            BukkitTask bukkitTask = new TimerRunnable(name).runTaskTimer(
                    instance, 0L, 20L
            );
            instance.getTimerHandler().setTaskId(bukkitTask.getTaskId());
            
            if (name.equalsIgnoreCase("Bombing"))
                  instance.getTimerHandler().setBombing(true);
      }
      
      @Subcommand("end")
      public void onTimerEnd(Player player, @Optional String strSilent) {
            Bukkit.getScheduler().cancelTask(instance.getTimerHandler().getTaskId());
            instance.getTimerHandler().setTaskId(0);
            instance.getTimerHandler().setBombing(false);
            if (strSilent != null && strSilent.equalsIgnoreCase("silent"))
                  instance.getTimerHandler().finishTimer();
      }
}
