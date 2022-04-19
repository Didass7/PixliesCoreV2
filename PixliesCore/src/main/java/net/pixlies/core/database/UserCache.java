package net.pixlies.core.database;

import com.google.gson.Gson;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.entity.user.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.BiConsumer;

public class UserCache {

    private final Config config = Main.getInstance().getConfig();
    private final JedisPool pool = new JedisPool(config.getString("redis.host", "127.0.0.1"), config.getInt("redis.port", 6379));
    private final Gson gson = new Gson();

    public User get(UUID uuid) {
        try (Jedis jedis = pool.getResource()) {
            return gson.fromJson(jedis.get("USER:" + uuid.toString()), User.class);
        }
    }

    public void put(UUID uuid, User user) {
        this.remove(uuid);
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
        if (!containsKey(uuid)) return;
        try (Jedis jedis = pool.getResource()) {
            jedis.del("USER:" + uuid.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forEach(BiConsumer<UUID, User> consumer) {
        for (User user : values()) {
            consumer.accept(user.getUniqueId(), user);
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
