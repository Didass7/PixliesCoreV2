package net.pixlies.core.scoreboard;

import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author MickMMars
 * @author Dynmie
 */
public class ScoreboardAdapter implements AssembleAdapter {

    private static final Main instance = Main.getInstance();

    private static final Map<UUID, Integer> scoreboardFrames = new HashMap<>();

    private static String[] frames(Player player) {
        //TODO: String c = Main.getInstance().getProfile(player.getUniqueId()).getFavoriteColour();
        // String nc = Methods.getNeighbourColor(c)+"";

        // TODO: Change this with pixlies and make the scoreboard be able to turn off from config
        // TODO:   because the nations module will also have it's own scoreboard system
        String c = "§a";
        String nc = "§2";
        return new String[]{
                "§f§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                "§f§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",
                c + "§lEARTH",

                c + "§lEART" + nc + "§lH",
                c + "§lEART" + nc + "§lH",
                c + "§lEAR" + nc + "§lT§f§lH",
                c + "§lEAR" + nc + "§lT§f§lH",
                c + "§lEA" + nc + "§lR§f§lTH",
                c + "§lEA" + nc + "§lR§f§lTH",
                c + "§lE" + nc + "§lA§f§lRTH",
                c + "§lE" + nc + "§lA§f§lRTH",
                nc + "§lE§f§lARTH",
                nc + "§lE§f§lARTH",
                "§f§lEARTH",
                "§f§lEARTH",

                nc+"§lE§f§lARTH",
                nc+"§lE§f§lARTH",
                nc+"§lE§f§lARTH",
                c+"§lE"+nc+"§lA§f§lRTH",
                c+"§lE"+nc+"§lA§f§lRTH",
                c+"§lEA"+nc+"§lR§f§lTH",
                c+"§lEA"+nc+"§lR§f§lTH",
                c+"§lEAR"+nc+"§lT§f§lH",
                c+"§lEAR"+nc+"§lT§f§lH",
                nc+"§lE"+c+"§lART"+nc+"§lH",
                nc+"§lE"+c+"§lART"+nc+"§lH",
                "§f§lE"+nc+"§lA"+c+"§lRTH",
                "§f§lE"+nc+"§lA"+c+"§lRTH",
                "§f§lEA"+nc+"§lR"+c+"§lTH",
                "§f§lEA"+nc+"§lR"+c+"§lTH",
                "§f§lEAR"+nc+"§lT"+c+"§lH",
                "§f§lEAR"+nc+"§lT"+c+"§lH",
                "§f§lEART"+nc+"§lH",
                "§f§lEART"+nc+"§lH"
        };
    }

    @Override
    public String getTitle(Player player) {

        scoreboardFrames.putIfAbsent(player.getUniqueId(), 0);
        int frame = scoreboardFrames.get(player.getUniqueId());
        if (frame == frames(player).length - 1) scoreboardFrames.put(player.getUniqueId(), 0);
        scoreboardFrames.put(player.getUniqueId(), scoreboardFrames.get(player.getUniqueId()) + 1);

        return frames(player)[frame];

    }

    // TODO: add scoreboard list stuff
    @Override
    public List<String> getLines(Player player) {

        User user = User.get(player.getUniqueId());

        ScoreboardType scoreboardType = user.getPersonalization().getScoreboardType();
        if (scoreboardType == null) return null;

        return switch (scoreboardType) {
            case STANDARD -> getStandard(player);
            case COMPACT -> getCompact(player);
            case DISABLED -> null;
        };

    }

    private List<String> getStandard(Player player) {
        User user = User.get(player.getUniqueId());

        List<String> lines = new ArrayList<>();

        // switch (user.getScoreboardType()) {} TODO
        if (user.getSettings().isPassive()) {

            boolean isInStaffMode = user.getSettings().isInStaffMode();
            boolean vanished = user.getSettings().isVanished();

            lines.add(""); // TODO: add line separator
            lines.add("§3§lStaff");
            lines.add("§3Staff Mode§8: " + (isInStaffMode ? "§aEnabled" : "§cDisabled"));
            lines.add("§3Vanish§8: " + (vanished ? "§aEnabled" : "§cDisabled"));
            lines.add("§3Game Mode§8: " + TextUtils.getGameModeFormatted(player.getGameMode()));

        }

        // TEST
        lines.add("");
        lines.add("§3§lTest");
        lines.add("§3Nick§8: §b" + user.getNickName());
        lines.add("§3isPassive§8: " + (user.getSettings().isPassive() ? "§aTrue" : "§cFalse"));
        lines.add("");

        return lines;
    }

    private List<String> getCompact(Player player) {
        // TODO
        return new ArrayList<>();
    }

    public enum ScoreboardType {

        STANDARD,
        COMPACT,
        DISABLED

    }

}
