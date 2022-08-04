package net.pixlies.nations.scoreboard;

import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.scoreboard.PixliesScoreboardAdapter;
import net.pixlies.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.Nation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter extends PixliesScoreboardAdapter {

    @NotNull
    @Override
    public String getExtraTitle(Player player) {
        return "&a&lEARTH";
    }

    @NotNull
    @Override
    public List<String> getCompact(Player player) {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public List<String> getStandard(Player player) {
        NationProfile profile = NationProfile.get(player.getUniqueId());
        return new ArrayList<>() {{
            if (profile.isInNation() && profile.getNation() != null) {
                Nation nation = profile.getNation();
                add("&3&lNation");
                add("&bOnline&7: " + nation.getOnlineMembers().size() + "/" + nation.getMembers().size());
            }
        }};
    }

    @NotNull
    @Override
    public List<Timer> getExtraTimers(Player player) {
        return new ArrayList<>();
    }

}
