package net.pixlies.lobby.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.handlers.impl.TimerHandler;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.pixlies.core.scoreboard.ScoreboardType;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import net.pixlies.lobby.managers.queue.Queue;
import net.pixlies.lobby.managers.queue.QueuePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ScoreboardAdapter implements AssembleAdapter {

    private static final Lobby instance = Lobby.getInstance();
    private static final TimerHandler timerHandler = Main.getInstance().getHandlerManager().getHandler(TimerHandler.class);
    private static final QueueManager queueManager = instance.getQueueManager();

    private static final Map<UUID, Integer> scoreboardFrames = new HashMap<>();
    private static String[] getFrames() {
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

    @Override
    public String getTitle(Player player) {
        scoreboardFrames.putIfAbsent(player.getUniqueId(), 0);
        int frame = scoreboardFrames.get(player.getUniqueId());
        if (frame == getFrames().length) {
            scoreboardFrames.put(player.getUniqueId(), 0);
            return getFrames()[0];
        }
        scoreboardFrames.put(player.getUniqueId(), scoreboardFrames.get(player.getUniqueId()) + 1);
        return getFrames()[frame];
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        User user = User.get(player.getUniqueId());
        ScoreboardType scoreboardType = user.getScoreboardType();
        if (scoreboardType == ScoreboardType.DISABLED) return null;

        // TIMERS
        if (user.hasTimers() || timerHandler.hasGlobalTimers()) {
            List<Timer> timers = new ArrayList<>(user.getTimers().stream().filter(timer -> !timer.isHidden()).toList());
            timers.addAll(timerHandler.getGlobalTimers().stream().filter(timer -> !timer.isHidden()).toList());
            if (!timers.isEmpty()) {
                lines.add("");
                // GLOBAL TIMERS
                // §c§lCombat§8: §700:00:00
                for (Timer timer : timers) {
                    lines.add(" " + timer.getColor().toString() + timer.getDisplayName() + "§7 §f" + timer.getRemainingFormatted());
                }
            }
        }

        // STAFF
        if (user.isPassive() || user.isBypassing()) {
            lines.add("");
            lines.add(" §bStaff Mode &7" + (user.isInStaffMode() && player.hasPermission("pixlies.moderation.staffmode") ? "On" : "Off"));
            lines.add(" §bVanish &7" + (user.isVanished() ? "On" : "Off"));
            lines.add(" §bBypass &7" + (user.isBypassing() ? "On" : "Off"));
        }

        lines.add("");

        // LINES
        lines.add(" &bLobby &7" + instance.getServer().getOnlinePlayers().size() + " Players");
        lines.add(" &bNetwork &7" + PlaceholderAPI.setPlaceholders(player, "%bungee_total%") + " Players");

        if (queueManager.isInQueue(player.getUniqueId())) {
            Queue queue = queueManager.getQueueFromPlayer(player.getUniqueId());
            if (queue != null) {
                QueuePlayer queuePlayer = queue.getQueuePlayer(player.getUniqueId());
                if (queuePlayer != null) {

                    lines.add("");
                    lines.add(" &bQueue &7" + getPausedFullColor(queue) + queue.getName());
                    lines.add(" &bPosition &7" + queuePlayer.getPosition() + "/" + queue.getSize());
                    lines.add(" &bOnline &7" + PlaceholderAPI.setPlaceholders(player, "%bungee_" + queue.getName() + "%") + (queue.getLimit() != -1 ? "/" + queue.getLimit() : ""));
                } // tree of death, ik
            }
        } else {
            lines.add("");
            lines.add("&7 Right click the banner");
            lines.add("&7 to select the server");
            lines.add("&7 you would like to join.");
        }
        // END LINES

        lines.add("                                                                  ");
        lines.add("&7&opixlies.net");

        return lines;
    }

    private static @NotNull String getPausedFullColor(Queue queue) {
        return (queue.isPaused() // if
                    ? "&c" // then
                : (queue.isOneMoreFull() // else if
                    ? "&e" // then
                : "") // else
        );
    }
}
