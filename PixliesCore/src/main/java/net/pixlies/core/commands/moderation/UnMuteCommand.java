package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class UnMuteCommand extends BaseCommand {

    @CommandAlias("unmute|mutent")
    @CommandPermission("pixlies.moderation.unmute")
    @CommandCompletion("@players")
    @Description("Mute'nt a player")
    public void onUnmute(CommandSender sender, String target, @Optional String silent) {
        OfflinePlayer targetOP = Bukkit.getOfflinePlayerIfCached(target);
        if (targetOP == null) {
            Lang.PLAYER_DOESNT_EXIST.send(sender);
            return;
        }

        User user = User.get(targetOP.getUniqueId());
        user.unmute(sender, silent != null && silent.equalsIgnoreCase("-s"));
    }

}
