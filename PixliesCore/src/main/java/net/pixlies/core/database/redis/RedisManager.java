package net.pixlies.core.database.redis;

import com.google.gson.*;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.utils.EventUtils;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;

import java.util.concurrent.ForkJoinPool;

public class RedisManager {

    private static final Main instance = Main.getInstance();
    private static final Config config = instance.getConfig();

    private final @Getter String channel = config.getString("redis.pubsub.channel", "PixliesCore");
    private JedisPool jedisPool;
    private JedisPubSub pubSub;

    private final String host = config.getString("redis.host", "localhost");
    private final String password = config.getString("redis.password", "");
    private final int port = config.getInt("redis.port", 6379);

    public RedisManager init() {
        // 20s timeout
        jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 20000, isAuthenticated() ? password : null);

        jedisPool.getResource();
        setupSubscribe();

        return this;
    }

    private void setupSubscribe() {
        this.pubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (!channel.equals(getChannel())) return;

                JsonObject jsonObject;
                try {
                    jsonObject = JsonParser.parseString(message).getAsJsonObject();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    return;
                }

                String identifier = jsonObject.get("identifier").getAsString();
                JsonObject data = jsonObject.get("data").getAsJsonObject();
                RedisMessageReceiveEvent event = new RedisMessageReceiveEvent(identifier, data);

                EventUtils.call(event);

            }
        };

        ForkJoinPool.commonPool().execute(() -> {
            try {
                Jedis jedis = jedisPool.getResource();

                if (isAuthenticated()) {
                    jedis.auth(password);
                }

                jedis.subscribe(pubSub, channel);
            } catch (JedisException e) {
                e.printStackTrace();
            }
        });

    }

    public void request(String channel, JsonObject jsonObject) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                if (isAuthenticated()) {
                    jedis.auth(password);
                }

                jedis.publish(channel, jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isConnected() {
        return jedisPool != null && !jedisPool.isClosed();
    }

    public boolean isAuthenticated() {
        return password != null && !password.isEmpty();
    }

    // ----------------------------------------------------------------------------- //
    // ---------------------------------- STATICS ---------------------------------- //
    // ----------------------------------------------------------------------------- //

    /**
     * Send a request to all listeners redis.
     * You should be using this method most of the time.
     * @param identifier The identifier for the object. ("StaffChat")
     * @param jsonToSend The JSON data to send.
     */
    public static void sendRequest(String identifier, JsonObject jsonToSend) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("identifier", identifier);
        jsonObject.add("data", jsonToSend);

        Main.getInstance().getRedisManager().request(Main.getInstance().getRedisManager().getChannel(), jsonObject);
    }

}
