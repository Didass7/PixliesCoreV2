package net.pixlies.core.handlers.impl;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.Assemble;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.AssembleStyle;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler implements Handler {

    private static final Main instance = Main.getInstance();

    @Getter private Assemble assemble;
    @Getter private final Scoreboard emptyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private final boolean enabled = Main.getInstance().getConfig().getBoolean("scoreboard.enabled", true);

    public void load(AssembleAdapter adapter) {
        if (!enabled) return;
        assemble = new Assemble(instance, adapter);
        assemble.setTicks(1);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    public void unload() {
        if (assemble == null) return;
        assemble.cleanup();
        assemble = null;
    }

}
