package net.pixlies.lobby.scoreboard;

import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.scoreboard.PixliesScoreboardAdapter;
import net.pixlies.core.utils.CC;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter extends PixliesScoreboardAdapter {

    @NotNull
    @Override
    public String getExtraTitle(Player player) {
        return CC.format("&x&4&E&D&E&D&B&lP&x&4&D&C&9&C&7&li&x&4&1&B&0&A&E&lx&x&3&B&A&1&9&F&ll&x&3&4&8&C&8&B&li&x&3&0&7&A&7&A&le&x&2&9&6&9&6&9&ls");
    }

    @NotNull
    @Override
    public List<String> getCompact(Player player) {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<String> getStandard(Player player) {
        return new ArrayList<>() {{
            add(CC.format("&3&lTest"));
        }};
    }

    @NotNull
    @Override
    public List<Timer> getExtraTimers(Player player) {
        return new ArrayList<>();
    }

}
