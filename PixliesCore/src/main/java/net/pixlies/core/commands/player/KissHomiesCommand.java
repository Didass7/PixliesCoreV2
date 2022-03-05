package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import lombok.val;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("kiss|kh|kisshomies")
public class KissHomiesCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onMessage(CommandSender sender, Player target, String message) {
        if (sender instanceof Player player) {
            val user = User.get(player.getUniqueId());
            if (user.isMuted() && !player.hasPermission("pixlies.moderation.mute.exempt")) {
                Lang.MUTE_MESSAGE.send(player);
                return;
            }
        }
        Lang.PLAYER_KISS.broadcast("%PLAYER%;" + sender.getName(), "%TARGET%;" + target.getName());
    }
}