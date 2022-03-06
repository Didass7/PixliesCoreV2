package net.pixlies.lobby.managers;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.config.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class QueueManager {

    private static final Lobby instance = Lobby.getInstance();
    private static final Config config = instance.getConfig();

    private final @Getter Map<String, Integer> queuePlayers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final @Getter Map<String, Integer> maxPlayers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final @Getter Map<String, Boolean> pausedQueue = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final @Getter Map<UUID, String> queueMap = new HashMap<>();
    private final @Getter Map<UUID, Integer> playerPosition = new HashMap<>();


    public boolean isInQueue(Player player) {
        return playerPosition.containsKey(player.getUniqueId());
    }

    public boolean isQueuePaused(String queue) {
        return pausedQueue.containsKey(queue) && pausedQueue.get(queue);
    }

    public String getQueue(Player player) {
        return queueMap.getOrDefault(player.getUniqueId(), null);
    }

    public boolean doesQueueExist(String queue) {
        return pausedQueue.containsKey(queue);
    }

    public int getQueuePosition(Player player) {
        return playerPosition.getOrDefault(player.getUniqueId(), -1);
    }

    public int getPlayersInQueue(String queue) {
        FileConfiguration bungeeconf = YamlConfiguration.loadConfiguration(new File("/home/minecraft/bungee/plugins/QueueBungee/config.yml"));
        return bungeeconf.getInt("servers." + queue + ".inQueue");
    }

    public int getQueueLimit(String queue) {
        return maxPlayers.getOrDefault(queue, -1);
    }

    public void addPlayerToQueue(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(player.getUniqueId().toString());
            out.writeUTF(server.toLowerCase());
            out.writeInt(getPriority(player));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Main.getInstance(), "queue:joinQueue", b.toByteArray());
    }

    public static int getPriority(Player player) {
        for (int i = 100; i > 0; i--) {
            if (player.hasPermission("pixlies.queue.priority." + i)) {
                return i;
            }
        }
        return 0;
    }

}
