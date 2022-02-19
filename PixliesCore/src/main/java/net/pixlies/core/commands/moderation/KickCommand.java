package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to kick the people you hate.
 * @author Dynmie
 */
public class KickCommand extends BaseCommand {

    @CommandAlias("kick")
    @CommandPermission("pixlies.moderation.kick")
    public void onKick(CommandSender sender, Player target, @Optional String reason) {

        User user = User.get(target.getUniqueId());
        String newReason = reason;
        boolean silent = false;

        if (target.hasPermission("pixlies.moderation.kick.exempt")) {
            Lang.MODERATION_CANNOT_KICK.send(sender);
            return;
        }

        if (reason != null && !reason.isEmpty()) {
            silent = reason.contains("-s");
            newReason = newReason.replace("-s", "");
        }

        user.kick(newReason, sender, silent);

    }

}
