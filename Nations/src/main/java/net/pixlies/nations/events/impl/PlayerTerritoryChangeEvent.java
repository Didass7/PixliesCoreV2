package net.pixlies.nations.events.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pixlies.nations.nations.chunk.NationChunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@ToString
public class PlayerTerritoryChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Getter @Setter private boolean cancelled = false;

    @Getter @NotNull private final Player player;

    @Getter @Nullable private final NationChunk from;
    @Getter @Nullable private final NationChunk to;

}
