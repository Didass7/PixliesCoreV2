package net.pixlies.lobby.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.scoreboard.PixliesScoreboardAdapter;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import net.pixlies.lobby.managers.queue.Queue;
import net.pixlies.lobby.managers.queue.QueuePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ScoreboardAdapter extends PixliesScoreboardAdapter {

    private static final Lobby instance = Lobby.getInstance();
    private static final QueueManager queueManager = instance.getQueueManager();

    private static final Map<UUID, Integer> scoreboardFrames = new HashMap<>();
    private static String[] frames(Player player) {
        return new String[] {
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",//
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",//
                "&x&3&c&a&4&a&2&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&2&9&6&9&6&9&lL&x&3&c&a&4&a&2&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&3&c&a&4&a&2&lL&x&2&9&6&9&6&9&lO&x&3&c&a&4&a&2&lB&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&3&c&a&4&a&2&lO&x&2&9&6&9&6&9&lB&x&3&c&a&4&a&2&lB&x&4&e&d&e&d&b&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&3&c&a&4&a&2&lB&x&2&9&6&9&6&9&lB&x&3&c&a&4&a&2&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&3&c&a&4&a&2&lB&x&2&9&6&9&6&9&lY",
                "&x&4&e&d&e&d&b&lL&x&4&e&d&e&d&b&lO&x&4&e&d&e&d&b&lB&x&4&e&d&e&d&b&lB&x&3&c&a&4&a&2&lY"
        };
    }

    @NotNull
    @Override
    public String getExtraTitle(Player player) {
        scoreboardFrames.putIfAbsent(player.getUniqueId(), 0);
        int frame = scoreboardFrames.get(player.getUniqueId());
        if (frame == frames(player).length) {
            scoreboardFrames.put(player.getUniqueId(), 0);
            return frames(player)[0];
        }
        scoreboardFrames.put(player.getUniqueId(), scoreboardFrames.get(player.getUniqueId()) + 1);
        return frames(player)[frame];
    }

    @NotNull
    @Override
    public List<String> getCompact(Player player) {
        return new ArrayList<>() {{
            add(CC.format("&bOnline&7: " + instance.getServer().getOnlinePlayers().size() + "/" + instance.getServer().getMaxPlayers()));
            add(CC.format("&bTotal&7: " + PlaceholderAPI.setPlaceholders(player, "%bungee_total%")));
        }};
    }

    @NotNull
    @Override
    public List<String> getStandard(Player player) {

        List<String> lines = new ArrayList<>();

        lines.add(CC.format("&3&lLobby"));
        lines.add(CC.format("&bOnline&7: " + instance.getServer().getOnlinePlayers().size() + "/" + instance.getServer().getMaxPlayers()));
        lines.add(CC.format("&bTotal&7: " + PlaceholderAPI.setPlaceholders(player, "%bungee_total%")));

        if (queueManager.isInQueue(player.getUniqueId())) {
            Queue queue = queueManager.getQueueFromPlayer(player.getUniqueId());
            if (queue != null) {
                QueuePlayer queuePlayer = queue.getQueuePlayer(player.getUniqueId());
                if (queuePlayer != null) {

                    lines.add("");
                    lines.add("&3&lQueue");
                    lines.add("&bServer&7: " + getPausedFullColor(queue) + queue.getName());
                    lines.add("&bPosition&7: " + queuePlayer.getPosition() + "/" + queue.getSize());
                    lines.add("&bOnline&7: " + PlaceholderAPI.setPlaceholders(player, "%bungee_" + queue.getName() + "%") + (queue.getLimit() != -1 ? "/" + queue.getLimit() : ""));
                } // tree of death, ik
            }
        }

        return lines;
    }

    private static @NotNull String getPausedFullColor(Queue queue) {
        return (queue.isPaused() ? "&c" : (queue.isOneMoreFull() ? "&e" : ""));
    }

    @NotNull
    @Override
    public List<Timer> getExtraTimers(Player player) {
        return new ArrayList<>();
    }

}
