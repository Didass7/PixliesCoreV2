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

    private final CommandSender sender;
    private final Player target;
    private final MessageType type;

    private final MessageHandler messageHandler = instance.getHandlerManager().getHandler(MessageHandler.class);

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public enum MessageType {
        MESSAGE, REPLY
    }

}
