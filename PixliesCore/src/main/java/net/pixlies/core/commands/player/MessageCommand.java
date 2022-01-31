package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.impl.MessageHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("message|w|msg|pm|m|whisper|tell|t")
public class MessageCommand extends BaseCommand {

    private final MessageHandler handler = Main.getInstance().getHandlerManager().getHandler(MessageHandler.class);

    @Default
    @CommandCompletion("@players @empty")
    @Syntax("<player> <message>")
    public void onMessage(Player player, Player target, String message) {
        val user = User.get(player.getUniqueId());
        if (user.getMute() != null) {
            Lang.MUTE_MESSAGE.send(player);
            return;
        }
        Lang.PLAYER_MESSAGE_FORMAT_TO.send(player, "%PLAYER%;" + target.getName(), "%MESSAGE%;" + message);
        Lang.PLAYER_MESSAGE_FORMAT_FROM.send(target, "%PLAYER%;" + player.getName(), "%MESSAGE%;" + message);
        handler.setReplyTarget(player.getUniqueId(), target.getUniqueId());
    }

}
