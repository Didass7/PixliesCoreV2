package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandPermission("pixlies.moderation.vanish")
public class JoinVanishedCommand extends BaseCommand {

    @CommandAlias("joinvanished")
    @Description("Set your vanish state when you join")
    public void onRun(Player player) {
        User user = User.get(player.getUniqueId());

        if (user.isJoinVanish()) {
            user.setJoinVanish(false);
            user.save();
            Lang.MODERATION_VANISH_JOIN_VANISH_DISABLED.send(player);
        } else {
            user.setJoinVanish(true);
            user.save();
            Lang.MODERATION_VANISH_JOIN_VANISH_ENABLED.send(player);
        }

    }

}
