package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.events.impl.player.PixliesSenderMessagePlayerEvent;
import net.pixlies.core.handlers.impl.MessageHandler;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.EventUtils;
import org.bukkit.entity.Player;

public class ReplyCommand extends BaseCommand {

    private final MessageHandler messageHandler = Main.getInstance().getHandlerManager().getHandler(MessageHandler.class);

    @CommandAlias("reply|r")
    @CommandCompletion("@empty")
    @Syntax("<message>")
    public void onReply(Player player, String message) {
        val user = User.get(player.getUniqueId());

        if (user.getMute() != null && !player.hasPermission("pixlies.moderation.mute.exempt")) {
            Lang.MUTE_MESSAGE.send(player);
            return;
        }

        val target = messageHandler.getReplyTarget(player.getUniqueId());

        if (target == null) {
            Lang.PLAYER_MESSAGE_NOONE_TO_MESSAGE.send(player);
            return;
        }

        val event = new PixliesSenderMessagePlayerEvent(player, target, message, PixliesSenderMessagePlayerEvent.MessageType.REPLY);
        EventUtils.call(event);
        if (event.isCancelled()) return;

        Lang.PLAYER_MESSAGE_FORMAT_TO.send(player, "%PLAYER%;" + target.getName(), "%MESSAGE%;" + event.getMessage());
        Lang.PLAYER_MESSAGE_FORMAT_FROM.send(target, "%PLAYER%;" + player.getName(), "%MESSAGE%;" + event.getMessage());
    }

}



