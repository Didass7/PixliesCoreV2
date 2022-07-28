package net.pixlies.core.commands.staff;

import co.aikar.commands.*;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Easier way to teleport around.
 * @author dynmie
 */
@CommandAlias("teleport|tp")
public class TpCommand extends BaseCommand {

    @Default
    @CommandPermission("pixlies.staff.tp")
    @Syntax("<x> <y> <z>")
    @Description("Teleport to another location")
    @CommandCompletion("@players @players @players")
    public void onTp(CommandSender sender, String[] args) {
        switch (args.length) {
            // SENDER TP TO PLAYER
            // NO CONSOLE
            case 1 -> {
                if (!(sender instanceof Player player)) {
                    throw new ConditionFailedException(MessageKeys.NOT_ALLOWED_ON_CONSOLE);
                }

                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    throw new InvalidCommandArgument(MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", args[0]);
                }

                player.teleport(target);

                Lang.STAFF_TELEPORT_SELF_TO_TARGET.send(sender,
                        "%PLAYER%;" + sender.getName(),
                        "%TARGET%;" + target.getName());
            }

            // SENDER TP PLAYER TO PLAYER / SENDER TO X Z COORDS
            case 2 -> {
                int x;
                int z;
                try {
                    x = Integer.parseInt(args[0]);
                    z = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    // SENDER TP PLAYER TO PLAYER
                    Player player = Bukkit.getPlayer(args[0]);
                    Player target = Bukkit.getPlayer(args[1]);
                    if (player == null || target == null || player.equals(sender) || target.equals(sender)) {
                        if (!(sender instanceof Player)) {
                            throw new ConditionFailedException(MessageKeys.NOT_ALLOWED_ON_CONSOLE);
                        }
                        throw new ConditionFailedException(Lang.PIXLIES + CC.format("&7That isn't a valid location."));
                    }

                    player.teleport(target);
                    Lang.STAFF_TELEPORT_PLAYER_TO_TARGET.send(sender,
                            "%PLAYER%;" + target.getName(),
                            "%TARGET%;" + target.getName());
                    return;
                }

                if (!(sender instanceof Player player)) {
                    throw new ConditionFailedException(MessageKeys.NOT_ALLOWED_ON_CONSOLE);
                }

                Location location = new Location(player.getWorld(), x, player.getLocation().getY(), z, player.getLocation().getYaw(), player.getLocation().getPitch());
                player.teleport(location);
                Lang.STAFF_TELEPORT_SELF_TO_TARGET.send(sender,
                        "%PLAYER%;" + sender.getName(),
                        "%TARGET%;" + TextUtils.getLocationFormatted(location));

            }

            // SENDER TP TO X Y Z
            // NO CONSOLE
            case 3 -> {
                if (!(sender instanceof Player player)) {
                    throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE);
                }

                int x;
                int y;
                int z;
                try {
                    x = Integer.parseInt(args[0]);
                    y = Integer.parseInt(args[1]);
                    z = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    throw new ConditionFailedException(Lang.PIXLIES + CC.format("&7That isn't a valid location."));
                }

                Location location = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
                player.teleport(location);
                Lang.STAFF_TELEPORT_SELF_TO_TARGET.send(player,
                        "%PLAYER%;" + player.getName(),
                        "%TARGET%;" + TextUtils.getLocationFormatted(location));
            }

            // PLAYER TO X Y Z
            case 4 -> {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    throw new InvalidCommandArgument(MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", args[0]);
                }

                int x;
                int y;
                int z;
                try {
                    x = Integer.parseInt(args[1]);
                    y = Integer.parseInt(args[2]);
                    z = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    throw new ConditionFailedException(Lang.PIXLIES + CC.format("&7That isn't a valid location."));
                }

                Location location = new Location(target.getWorld(), x, y, z, target.getLocation().getYaw(), target.getLocation().getPitch());
                target.teleport(location);
                Lang.STAFF_TELEPORT_PLAYER_TO_TARGET.send(sender,
                        "%PLAYER%;" + target.getName(),
                        "%TARGET%;" + TextUtils.getLocationFormatted(location));
            }

            default -> throw new InvalidCommandArgument();
        }
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
