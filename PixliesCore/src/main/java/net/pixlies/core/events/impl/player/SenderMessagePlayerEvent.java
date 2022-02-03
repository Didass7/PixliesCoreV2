package net.pixlies.core.events.impl.player;

import net.pixlies.core.events.PixliesCancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.handlers.impl.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class SenderMessagePlayerEvent extends PixliesCancellableEvent {

    private final CommandSender sender;
    private final Player target;
    private final MessageType type;

    private final MessageHandler messageHandler = instance.getHandlerManager().getHandler(MessageHandler.class);

    public enum MessageType {
        MESSAGE, REPLY
    }

}
