package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandPermission("pixlies.moderation.commandspy")
public class CommandSpyCommand extends BaseCommand {

    @CommandAlias("commandspy")
    @Description("View all players and the commands they run.")
    public void onRun(Player player) {
        User user = User.get(player.getUniqueId());

        if (user.isCommandSpyEnabled()) {
            user.setCommandSpyEnabled(false);
            user.save();
            Lang.MODERATION_COMMANDSPY_DISABLED.send(player);
        } else {
            user.setCommandSpyEnabled(true);
            user.save();
            Lang.MODERATION_COMMANDSPY_ENABLED.send(player);
        }

    }

}
