package net.pixlies.core.runnables.impl;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TabListHandler;
import net.pixlies.core.runnables.PixliesRunnable;
import org.bukkit.entity.Player;

public class TabListRunnable extends PixliesRunnable {

    private static final Main instance = Main.getInstance();
    private static final TabListHandler handler = instance.getHandlerManager().getHandler(TabListHandler.class);

    public TabListRunnable() {
        super(false, 30, 5);
    }

    @Override
    public void run() {

        if (!handler.isLoaded()) {
            return;
        }

        for (Player player : instance.getServer().getOnlinePlayers()) {
            handler.formatTabListFor(player);
            handler.updateDisplayNames();
        }

    }

}
