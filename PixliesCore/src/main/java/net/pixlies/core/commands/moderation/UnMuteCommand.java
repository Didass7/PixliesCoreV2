package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class UnMuteCommand extends BaseCommand {

    @CommandAlias("unmute|mutent")
    @CommandPermission("pixlies.moderation.unmute")
    @CommandCompletion("@players")
    @Description("Mute'nt a player")
    public void onUnmute(CommandSender sender, OfflinePlayer target, @Optional String s) {

        boolean silent = s != null && s.contains("-s");
        User user = User.get(target.getUniqueId());

        if (!user.isMuted()) {
            Lang.PLAYER_NOT_MUTED.send(sender);
            return;
        }

        user.unmute(sender, silent);

    }

}
