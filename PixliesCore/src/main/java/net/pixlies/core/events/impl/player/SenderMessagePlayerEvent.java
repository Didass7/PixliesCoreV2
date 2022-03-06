package net.pixlies.core.events.impl.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.events.PixliesCancellableEvent;
import net.pixlies.core.handlers.impl.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class SenderMessagePlayerEvent extends PixliesCancellableEvent {

    @Getter private static final HandlerList handlerList = new HandlerList();
    private final MessageHandler messageHandler = pixlies.getHandlerManager().getHandler(MessageHandler.class);

    private final CommandSender sender;
    private final Player target;
    private final MessageType type;

    public enum MessageType {
        MESSAGE, REPLY
    }

}
