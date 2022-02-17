package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;

@CommandAlias("kickall")
@CommandPermission("pixlies.moderation.kickall")
public class KickallCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @Description("Kick all players")
    @CommandCompletion("@empty")
    public void onKickAll(CommandSender sender, @Optional String reason) {
        for (User user : User.getOnlineUsers()) {
            user.kick(reason);
        }
        Lang.MODERATION_ISSUED_KICKALL.broadcast("%PLAYER%;" + sender.getName(), "%REASON%;" + reason);
    }

}
