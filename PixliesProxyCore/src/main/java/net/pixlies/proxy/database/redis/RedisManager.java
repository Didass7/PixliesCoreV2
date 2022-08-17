package net.pixlies.proxy.database.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.utils.EventUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;

import java.util.concurrent.ForkJoinPool;

public class RedisManager {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final Config config = instance.getConfig();

    private final @Getter String channel = config.getConfig().getString("redis.pubsub.channel", "PixliesCore");
    private JedisPool jedisPool;
    private JedisPubSub pubSub;

    private final String host = config.getConfig().getString("redis.host", "localhost");
    private final String password = config.getConfig().getString("redis.password", "");
    private final int port = config.getConfig().getInt("redis.port", 6379);

    public void init() {
        // 20s timeout
        jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 20000, isAuthenticated() ? password : null);

        jedisPool.getResource();
        setupSubscribe();
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
                EventUtils.callEvent(event);

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
        try (Jedis jedis = jedisPool.getResource()) {
            if (isAuthenticated()) {
                jedis.auth(password);
            }

            jedis.publish(channel, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        PixliesProxy.getInstance().getRedisManager().request(PixliesProxy.getInstance().getRedisManager().getChannel(), jsonObject);
    }

}
