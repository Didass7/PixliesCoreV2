package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class MuteCommand extends BaseCommand {

    @CommandAlias("mute")
    @CommandPermission("pixlies.moderation.mute")
    @CommandCompletion("@players")
    @Description("Mutes player with the default reason")
    public void onBan(CommandSender sender, String player, @Optional String reason) {
        boolean silent = false;
        String muteReason = Main.getInstance().getConfig().getString("moderation.defaultReason", "No reason given");
        if (reason != null && !reason.isEmpty()) {
            muteReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        OfflinePlayer targetOP = Bukkit.getOfflinePlayerIfCached(player);
        if (targetOP == null) {
            Lang.PLAYER_DOESNT_EXIST.send(sender);
            return;
        }

        User user = User.get(targetOP.getUniqueId());
        user.mute(muteReason, sender, silent);
    }

}
