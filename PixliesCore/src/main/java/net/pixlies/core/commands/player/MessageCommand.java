package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.events.impl.player.PixliesSenderMessagePlayerEvent;
import net.pixlies.core.handlers.impl.MessageHandler;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.ranks.Rank;
import net.pixlies.core.utils.EventUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("message|w|msg|pm|whisper|tell|t")
public class MessageCommand extends BaseCommand {

    private final MessageHandler handler = Main.getInstance().getHandlerManager().getHandler(MessageHandler.class);

    @Default
    @CommandCompletion("@players @empty")
    @Syntax("<player> <message>")
    public void onMessage(CommandSender sender, OnlinePlayer targetPlayer, String message) {
        Player target = targetPlayer.getPlayer();

        if (sender instanceof Player player) {
            val user = User.get(player.getUniqueId());
            if (user.getActiveMute() != null && !player.hasPermission("pixlies.moderation.mute.exempt")) {
                Lang.MUTE_MESSAGE.send(player);
                return;
            }
            handler.setReplyTarget(player.getUniqueId(), target.getUniqueId());
        }

        val event = new PixliesSenderMessagePlayerEvent(sender, target, message, PixliesSenderMessagePlayerEvent.MessageType.MESSAGE);
        EventUtils.call(event);
        if (event.isCancelled()) return;

        Rank senderRank = sender instanceof Player player ? Rank.getRank(player.getUniqueId()) : Rank.DEFAULT;
        Rank targetRank = Rank.getRank(target.getUniqueId());

        Lang.PLAYER_MESSAGE_FORMAT_TO.send(sender, "%PLAYER%;" + targetRank.getColor() + target.getName(), "%MESSAGE%;" + event.getMessage());
        Lang.PLAYER_MESSAGE_FORMAT_FROM.send(target, "%PLAYER%;" + senderRank.getColor() + sender.getName(), "%MESSAGE%;" + event.getMessage());
    }

}
