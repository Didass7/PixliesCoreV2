package net.pixlies.core.database;

import com.google.gson.Gson;
import net.pixlies.core.entity.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class UserCache {

    private final JedisPool pool;
    private final Gson gson;

    public UserCache() {
        pool = new JedisPool("localhost", 6379);
        gson = new Gson();
    }

    public User get(UUID uuid) {
        try(Jedis jedis = pool.getResource()) {
            return gson.fromJson(jedis.get("USER:" + uuid.toString()), User.class);
        }
    }

    public void put(UUID uuid, User user) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set("USER:" + uuid.toString(), gson.toJson(user));
        }
    }

    public boolean containsKey(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists("USER:" + uuid.toString());
        }
    }

    public User getOrDefault(UUID uuid, User dVal) {
        if (containsKey(uuid)) return get(uuid);
        return dVal;
    }

    public void remove(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del("USER:" + uuid.toString());
        }
    }

}
