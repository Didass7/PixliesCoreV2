package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class MuteCommand extends BaseCommand {

    @CommandAlias("mute")
    @CommandPermission("pixlies.moderation.mute")
    @CommandCompletion("@players")
    @Description("Mutes player with the default reason")
    public void onBan(CommandSender sender, OfflinePlayer target, @Optional String reason) {
        boolean silent = false;
        String muteReason = Main.getInstance().getConfig().getString("moderation.defaultReason", "No reason given");
        if (reason != null && !reason.isEmpty()) {
            muteReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        User user = User.get(target.getUniqueId());
        user.mute(muteReason, sender, silent);
    }

}
