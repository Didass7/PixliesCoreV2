package net.pixlies.nations.scoreboard;

import net.pixlies.core.scoreboard.PixliesTabAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter implements PixliesTabAdapter {

    @Override
    public @NotNull List<String> getHeader(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("      &r&x&4&E&D&E&D&B&lP&r&x&4&D&C&9&C&7&li&r&x&4&1&B&0&A&E&lx&r&x&3&B&A&1&9&F&ll&r&x&3&4&8&C&8&B&li&r&x&3&0&7&A&7&A&le&r&x&2&9&6&9&6&9&ls&r&x&9&C&9&C&9&C&lN&r&x&A&8&A&8&A&8&le&r&x&A&D&A&D&A&D&lt&r&x&B&3&B&3&B&3&lw&r&x&B&D&B&D&B&D&lo&r&x&D&4&D&4&D&4&lr&r&f&lk&r&7!      "); // PixliesNetwork!
        lines.add("§x§2§E§D§C§3§E§lE§x§3§0§C§A§3§E§lA§x§3§1§B§F§3§E§lR§x§3§8§B§2§4§3§lT§x§3§7§A§3§4§1§lH"); // EARTH
        lines.add("");

        return lines;
    }

    @Override
    public @NotNull List<String> getFooter(Player player) {
        return new ArrayList<>() {{
            add("");
            add("&r&x&4&D&C&9&C&7www.pixlies.net");
            add("&r&x&4&D&C&9&C&7pixlies.net/discord");
            add("");
        }};
    }

}
