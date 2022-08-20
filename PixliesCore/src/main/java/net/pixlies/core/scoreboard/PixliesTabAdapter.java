package net.pixlies.core.scoreboard;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PixliesTabAdapter {

    @NotNull List<String> getHeader(Player player);

    @NotNull List<String> getFooter(Player player);

}
