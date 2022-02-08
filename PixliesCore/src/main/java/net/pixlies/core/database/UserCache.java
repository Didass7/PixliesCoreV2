package net.pixlies.core.database;

import com.google.gson.Gson;
import net.pixlies.core.entity.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.BiConsumer;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean containsKey(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists("USER:" + uuid.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getOrDefault(UUID uuid, User dVal) {
        if (containsKey(uuid)) return get(uuid);
        return dVal;
    }

    public void remove(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del("USER:" + uuid.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forEach(BiConsumer<UUID, User> consumer) {
        for (User user : values()) {
            consumer.accept(user.getUuid(), user);
        }
    }

    public Collection<User> values() {
        Collection<User> re = new HashSet<>();
        try (Jedis jedis = pool.getResource()) {
            for (String key : jedis.keys("USER:*")) {
                re.add(get(UUID.fromString(key.replace("USER:", ""))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

}
