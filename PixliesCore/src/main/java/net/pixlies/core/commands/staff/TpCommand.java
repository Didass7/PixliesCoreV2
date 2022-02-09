package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Easier way to teleport around.
 * @author dynmie
 */
@CommandAlias("teleport|tp")
@CommandPermission("pixlies.staff.tp")
public class TpCommand extends BaseCommand {

    // CONSOLE
    @Description("Teleport a player to another player")
    @CommandCompletion("@players @players")
    public void onTpSender(CommandSender sender, Player player, Player target) {
        player.teleport(target);
        Lang.STAFF_TELEPORT_PLAYER_TO_TARGET.send(sender,
                "%PLAYER%;" + player.getName(),
                "%TARGET%;" + target.getName());
    }

    // PLAYER
    @Description("Teleport to another player")
    @CommandCompletion("@players")
    public void onTpSender(Player player, Player target) {
        player.teleport(target);
        Lang.STAFF_TELEPORT_PLAYER_TO_TARGET.send(player,
                "%PLAYER%;" + player.getName(),
                "%TARGET%;" + target.getName());
    }

    // CONSOLE
    @Description("Teleport a player to a location")
    @CommandCompletion("@players @empty")
    public void onTpSender(CommandSender sender, Player player, Location location) {
        player.teleport(location);
        Lang.STAFF_TELEPORT_PLAYER_TO_TARGET.send(sender,
                "%PLAYER%;" + player.getName(),
                "%TARGET%;" + TextUtils.getLocationFormatted(location));
    }

    // PLAYER
    @Description("Teleport to a location")
    @CommandCompletion("@empty")
    public void onTpSender(Player player, Location location) {
        player.teleport(location);
        Lang.STAFF_TELEPORT_SELF_TO_TARGET.send(player,
                "%PLAYER%;" + player.getName(),
                "%TARGET%;" + TextUtils.getLocationFormatted(location));
    }

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
