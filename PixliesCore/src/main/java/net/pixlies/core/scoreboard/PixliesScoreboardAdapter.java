package net.pixlies.core.scoreboard;

import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.handlers.impl.TimerHandler;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class PixliesScoreboardAdapter implements AssembleAdapter {

    private static final Main instance = Main.getInstance();
    private static final TimerHandler timerHandler = instance.getHandlerManager().getHandler(TimerHandler.class);

    @Override
    public String getTitle(Player player) {
        return getExtraTitle(player);
    }

    @Override
    public List<String> getLines(Player player) {

        //       EARTH
        //
        // Staff
        //  Staff Mode: Disabled
        //  Vanish: Enabled
        //  Game Mode: Creative
        //
        // Nation/Lobby
        //  Online: 1/10
        //  Placeholder: 100%
        //
        // Reboot: 5:47
        // Nation: 01:00:00
        // Placeholder: 04:32
        //

        List<String> lines = new ArrayList<>();

        User user = User.get(player.getUniqueId());
        ScoreboardType scoreboardType = user.getScoreboardType();

        if (user.getSettings().isPassive()) {

            boolean isInStaffMode = user.getSettings().isInStaffMode();
            boolean vanished = user.getSettings().isVanished();

            // TODO: make this look pretty
            lines.add("");
            lines.add("§3§lStaff");
            lines.add("§3Staff Mode§8: " + (isInStaffMode ? "§aEnabled" : "§cDisabled"));
            lines.add("§3Vanish§8: " + (vanished ? "§aEnabled" : "§cDisabled"));
            lines.add("§3Game Mode§8: " + TextUtils.getGameModeFormatted(player.getGameMode()));

        }

        List<String> extraLines = switch (scoreboardType) {
            case COMPACT -> getCompact(player);
            case DISABLED -> new ArrayList<>(); // empty list
            default -> getStandard(player); // case STANDARD
        };

        lines.add("                    "); // spacing
        lines.addAll(extraLines);

        List<Timer> extraTimers = getExtraTimers(player);

        if (user.hasTimers() || timerHandler.hasGlobalTimers() || !extraTimers.isEmpty()) {
            lines.add("");

            // GLOBAL TIMERS
            for (Timer timer : timerHandler.getGlobalTimers()) {
                lines.add(timer.getColor().toString() + "§l" + timer.getDisplayName() + "§8: §7" + timer.getRemainingFormatted());
                // §c§lCombat§8: §700:00:00
            }

            // EXTRA TIMERS
            for (Timer timer : extraTimers) {
                lines.add(timer.getColor().toString() + "§l" + timer.getDisplayName() + "§8: §7" + timer.getRemainingFormatted());
                // §c§lCombat§8: §700:00:00
            }

            // USER TIMERS
            for (Timer timer : user.getTimers()) {
                lines.add(timer.getColor().toString() + "§l" + timer.getDisplayName() + "§8: §7" + timer.getRemainingFormatted());
                // §c§lCombat§8: §700:00:00
            }

        }

        lines.add("");
        lines.add("§bpixlies.net");

        return lines;
    }

    public abstract @NotNull String getExtraTitle(Player player);

    public abstract @NotNull List<String> getCompact(Player player);

    public abstract @NotNull List<String> getStandard(Player player);

    public abstract @NotNull List<Timer> getExtraTimers(Player player);

}
