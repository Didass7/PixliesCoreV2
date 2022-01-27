package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("world")
@CommandPermission("pixlies.staff.world")
public class WorldCommand extends BaseCommand {

    @Default
    @Private
    @CommandCompletion("@worlds")
    @Syntax("<world>")
    public void onWorld(Player player, World world) {
        player.teleport(world.getSpawnLocation());
        Lang.STAFF_WORLD_TELEPORT.send(player);
    }

    @CommandPermission("pixlies.staff.world.others")
    @CommandCompletion("@players @worlds")
    public void onWorld(CommandSender sender, Player target, World world) {
        target.teleport(world.getSpawnLocation());
        Lang.STAFF_WORLD_TELEPORT.send(sender);
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
