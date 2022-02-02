package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.entity.User;
import net.pixlies.core.utils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandAlias("tempmute")
@CommandPermission("pixlies.moderation.tempmute")
public class TempMuteCommand extends BaseCommand {

    @CommandCompletion("@players")
    @Description("Temporarily mutes player with the default reason")
    public void onTempMute(CommandSender sender, String player, String duration, @Optional String reason) {
        boolean silent = false;
        long durationLong = TimeUnit.getDuration(duration);
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
        user.tempMute(muteReason, sender, durationLong, silent);
    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }


}
