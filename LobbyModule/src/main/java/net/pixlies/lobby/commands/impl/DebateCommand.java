package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandAlias("debate")
@Description("Teleport to the Debate Hall")
public class DebateCommand extends BaseCommand {
      @Default
      public void onDebate(Player player) {
            World world = Bukkit.getWorld("debateWorld");
            Location location = new Location(world, 73, -50, -22);
            player.teleport(location);
            player.sendMessage(Lang.PIXLIES + "You have now warped to the §aDebate Hall§7.");
      }
}
