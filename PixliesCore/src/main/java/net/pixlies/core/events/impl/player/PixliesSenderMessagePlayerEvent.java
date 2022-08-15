package net.pixlies.core.events.impl.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class PixliesSenderMessagePlayerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    private @Setter boolean cancelled = false;

    private final MessageHandler messageHandler = Main.getInstance().getHandlerManager().getHandler(MessageHandler.class);

    private final CommandSender sender;
    private final Player target;
    private final MessageType type;

    public enum MessageType {
        MESSAGE, REPLY
    }

}
