package net.pixlies.lobby.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.scoreboard.PixliesScoreboardAdapter;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ScoreboardAdapter extends PixliesScoreboardAdapter {

    private static final Lobby instance = Lobby.getInstance();

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
        return new ArrayList<>() {{
            add(CC.format("&3&lLobby"));
            add(CC.format("&bOnline&7: " + instance.getServer().getOnlinePlayers().size() + "/" + instance.getServer().getMaxPlayers()));
            add(CC.format("&bTotal&7: " + PlaceholderAPI.setPlaceholders(player, "%bungee_total%")));
        }};
    }

    @NotNull
    @Override
    public List<Timer> getExtraTimers(Player player) {
        return new ArrayList<>();
    }

}
