package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to teleport between worlds.
 * @author dynmie
 */
@CommandAlias("world")
@CommandPermission("pixlies.staff.world")
public class WorldCommand extends BaseCommand {

    @Default
    @CommandCompletion("@worlds @players")
    @Syntax("<world>")
    public void onWorld(CommandSender sender, World world, Player target) {

        if (sender instanceof Player player && player.getUniqueId().equals(target.getUniqueId())) {
            Location location = world.getSpawnLocation();
            player.teleport(location);
            Lang.STAFF_TELEPORT_SELF_TO_TARGET.send(player, "%TARGET%;" + TextUtils.getLocationFormatted(location));
            return;
        }

        if (sender.hasPermission("pixlies.staff.world.others")) {
            Location location = world.getSpawnLocation();
            target.teleport(location);
            Lang.STAFF_TELEPORT_PLAYER_TO_TARGET.send(sender, "%PLAYER%;" + target.getName(),
                    "%TARGET%;" + TextUtils.getLocationFormatted(location));
            return;
        }

        throw new ConditionFailedException(MessageKeys.PERMISSION_DENIED_PARAMETER);
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
