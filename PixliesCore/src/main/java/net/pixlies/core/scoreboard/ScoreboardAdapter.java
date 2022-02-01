package net.pixlies.core.scoreboard;

import net.pixlies.core.Main;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.AssembleAdapter;
import org.bukkit.entity.Player;
import java.util.*;

public class ScoreboardAdapter implements AssembleAdapter {

    private static final Main instance = Main.getInstance();

    private static final Map<UUID, Integer> scoreboardFrames = new HashMap<>();

    private static String[] frames(Player player) {
        //TODO: String c = Main.getInstance().getProfile(player.getUniqueId()).getFavoriteColour();
        // String nc = Methods.getNeighbourColor(c)+"";
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
        // if (Main.getInstance().getUtilLists().staffMode.contains(player.getUniqueId())) {
        if (true) {
            return "§eStaff - " +  frames(player)[frame];
        } else {
            return frames(player)[frame];
        }
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> returnable = new ArrayList<>();
        //TODO
        return returnable;
    }

    public enum scoreboardType {

        STANDARD,
        COMPACT

    }

}
