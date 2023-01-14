package net.pixlies.nations.scoreboard;

import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.handlers.impl.TimerHandler;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.pixlies.core.scoreboard.ScoreboardType;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter {

    private static final TimerHandler timerHandler = Main.getInstance().getHandlerManager().getHandler(TimerHandler.class);

    @Override
    public String getTitle(Player player) {
        // TODO: Animated EARTH
        return "§x§2§E§D§C§3§E§lE§x§3§0§C§A§3§E§lA§x§3§1§B§F§3§E§lR§x§3§8§B§2§4§3§lT§x§3§7§A§3§4§1§lH";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        User user = User.get(player.getUniqueId());
        NationProfile profile = NationProfile.get(player.getUniqueId());
        ScoreboardType scoreboardType = user.getScoreboardType();
        if (scoreboardType == ScoreboardType.DISABLED) return null;

        // STAFF
        if (user.isPassive() || user.isBypassing()) {
            lines.add("");
            lines.add("§3§lStaff");
            lines.add("§bStaff Mode§7: " + (user.isInStaffMode() && player.hasPermission("pixlies.moderation.staffmode") ? "Enabled" : "Disabled"));
            lines.add("§bVanish§7: " + (user.isVanished() ? "Enabled" : "Disabled"));
            lines.add("§bBypass§7: " + (user.isBypassing() ? "Enabled" : "Disabled"));
        }

        lines.add("                    "); // spacing

        // LINES
        lines.add("&3&l" + player.getName());
        lines.add("&bBalance&7: &6" + profile.getBalance() + " coins");

        if (profile.isInNation() && profile.getNation() != null) {
            Nation nation = profile.getNation();
            lines.add("");
            lines.add("&3&lNation");
            lines.add("&bOnline&7: " + nation.getOnlineMembers().size() + "/" + nation.getMembers().size());
        }
        // END LINES

        // TIMERS
        if (user.hasTimers() || timerHandler.hasGlobalTimers()) {
            List<Timer> timers = new ArrayList<>(user.getTimers().stream().filter(timer -> !timer.isHidden()).toList());
            timers.addAll(timerHandler.getGlobalTimers().stream().filter(timer -> !timer.isHidden()).toList());
            if (!timers.isEmpty()) {
                lines.add("");
                // GLOBAL TIMERS
                // §c§lCombat§8: §700:00:00
                for (Timer timer : timers) {
                    lines.add(timer.getColor().toString() + "§l" + timer.getDisplayName() + "§7: §f" + timer.getRemainingFormatted());
                }
            }
        }

        lines.add("");
        lines.add("§bpixlies.net");

        return lines;
    }

}
