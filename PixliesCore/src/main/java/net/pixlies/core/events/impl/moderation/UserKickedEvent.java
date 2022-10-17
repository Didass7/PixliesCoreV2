package net.pixlies.core.events.impl.moderation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.pixlies.core.entity.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@ToString
public class UserKickedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Getter private final CommandSender executor;
    @Getter private final Player player;
    @Getter private final User user;
    @Getter private final String reason;
    @Getter private final boolean silent;

}
