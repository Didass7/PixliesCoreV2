package net.pixlies.proxy.database.redis;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class RedisMessageReceiveEvent extends Event {
    private final String identifier;

    private final JsonObject data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisMessageReceiveEvent that = (RedisMessageReceiveEvent) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, data);
    }

}
