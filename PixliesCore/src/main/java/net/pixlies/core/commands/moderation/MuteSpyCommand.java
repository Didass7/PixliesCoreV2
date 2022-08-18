package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandPermission("pixlies.moderation.mutespy")
public class MuteSpyCommand extends BaseCommand {

    @CommandAlias("mutespy")
    @Description("See muted players speak")
    public void onRun(Player player) {
        User user = User.get(player.getUniqueId());

        if (user.isViewMutedChat()) {
            user.setViewMutedChat(false);
            user.save();
            Lang.MODERATION_MUTESPY_DISABLED.send(player);
        } else {
            user.setViewMutedChat(true);
            user.save();
            Lang.MODERATION_MUTESPY_ENABLED.send(player);
        }

    }

}
