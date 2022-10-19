package net.pixlies.core.database.redis;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RedisMessageReceiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public RedisMessageReceiveEvent(String identifier, JsonObject data) {
        super(true);
        this.identifier = identifier;
        this.data = data;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    private final @Getter String identifier;
    private final @Getter JsonObject data;

}
