package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandPermission("pixlies.moderation.socialspy")
public class SocialSpyCommand extends BaseCommand {

    @CommandAlias("socialspy")
    @Description("View social messages")
    public void onRun(Player player) {
        User user = User.get(player.getUniqueId());

        if (user.isSocialSpyEnabled()) {
            user.setSocialSpyEnabled(false);
            user.save();
            Lang.MODERATION_SOCIALSPY_DISABLED.send(player);
        } else {
            user.setSocialSpyEnabled(true);
            user.save();
            Lang.MODERATION_SOCIALSPY_ENABLED.send(player);
        }

    }

}
