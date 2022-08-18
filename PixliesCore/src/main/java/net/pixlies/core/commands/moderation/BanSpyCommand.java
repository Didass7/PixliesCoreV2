package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandPermission("pixlies.moderation.banspy")
public class BanSpyCommand extends BaseCommand {

    @CommandAlias("banspy")
    @Description("View banned players joining and leaving")
    public void onRun(Player player) {
        User user = User.get(player.getUniqueId());

        if (user.isViewBannedJoins()) {
            user.setViewBannedJoins(false);
            user.save();
            Lang.MODERATION_BANSPY_DISABLED.send(player);
        } else {
            user.setViewBannedJoins(true);
            user.save();
            Lang.MODERATION_BANSPY_ENABLED.send(player);
        }

    }

}
