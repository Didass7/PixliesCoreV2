package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("time")
public class TimeCommand extends BaseCommand {

    @Subcommand("set")
    @Syntax("<time>")
    public void onSet(CommandSender sender, Long time) {
        World world = sender instanceof Player player ? player.getWorld() : Bukkit.getWorlds().get(0);
        if (world == null) {
            throw new ConditionFailedException("There is no world.");
        }
        world.setTime(time);
        Lang.STAFF_TIME_SET.send(sender, "%WORLD%;" + world.getName(), "%TIME%;" + time);
    }

    @CommandAlias("day")
    @Subcommand("day")
    public void onDay(CommandSender sender) {
        World world = sender instanceof Player player ? player.getWorld() : Bukkit.getWorlds().get(0);
        if (world == null) {
            throw new ConditionFailedException("There is no world.");
        }
        world.setTime(1000);
        Lang.STAFF_TIME_SET.send(sender, "%WORLD%;" + world.getName(), "%TIME%;1000");
    }

    @CommandAlias("dawn")
    @Subcommand("dawn")
    public void onDawn(CommandSender sender) {
        World world = sender instanceof Player player ? player.getWorld() : Bukkit.getWorlds().get(0);
        if (world == null) {
            throw new ConditionFailedException("There is no world.");
        }
        world.setTime(0);
        Lang.STAFF_TIME_SET.send(sender, "%WORLD%;" + world.getName(), "%TIME%;0");
    }

    @CommandAlias("midday")
    @Subcommand("midday")
    public void onMidday(CommandSender sender) {
        World world = sender instanceof Player player ? player.getWorld() : Bukkit.getWorlds().get(0);
        if (world == null) {
            throw new ConditionFailedException("There is no world.");
        }
        world.setTime(6000);
        Lang.STAFF_TIME_SET.send(sender, "%WORLD%;" + world.getName(), "%TIME%;6000");
    }

    @CommandAlias("dusk")
    @Subcommand("dusk")
    public void onDusk(CommandSender sender) {
        World world = sender instanceof Player player ? player.getWorld() : Bukkit.getWorlds().get(0);
        if (world == null) {
            throw new ConditionFailedException("There is no world.");
        }
        world.setTime(12000);
        Lang.STAFF_TIME_SET.send(sender, "%WORLD%;" + world.getName(), "%TIME%;12000");
    }

    @CommandAlias("night")
    @Subcommand("night")
    public void onNight(CommandSender sender) {
        World world = sender instanceof Player player ? player.getWorld() : Bukkit.getWorlds().get(0);
        if (world == null) {
            throw new ConditionFailedException("There is no world.");
        }
        world.setTime(13000);
        Lang.STAFF_TIME_SET.send(sender, "%WORLD%;" + world.getName(), "%TIME%;13000");
    }

    @CommandAlias("midnight")
    @Subcommand("midnight")
    public void onMidnight(CommandSender sender) {
        World world = sender instanceof Player player ? player.getWorld() : Bukkit.getWorlds().get(0);
        if (world == null) {
            throw new ConditionFailedException("There is no world.");
        }
        world.setTime(18000);
        Lang.STAFF_TIME_SET.send(sender, "%WORLD%;" + world.getName(), "%TIME%;18000");
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
