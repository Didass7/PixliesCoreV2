package net.pixlies.core.events.impl.moderation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.moderation.Punishment;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@ToString
public class UserPunishedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Getter private final CommandSender executor;
    @Getter private final User user;
    @Getter private final Punishment punishment;
    @Getter private final boolean silent;

}
