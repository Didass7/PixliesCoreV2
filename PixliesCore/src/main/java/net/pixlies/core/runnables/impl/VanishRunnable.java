package net.pixlies.core.runnables.impl;

import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.VanishHandler;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.runnables.PixliesRunnable;

public class VanishRunnable extends PixliesRunnable {

    private final VanishHandler handler = Main.getInstance().getHandlerManager().getHandler(VanishHandler.class);

    public VanishRunnable() {
        super(false, 10, 0);
    }

    @Override
    public void run() {
        Main.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            if (!handler.isVanished(player.getUniqueId())) return;
            player.sendActionBar(Component.text(Lang.MODERATION_VANISH_NO_ONE_CAN_SEE.get(player)));
        });
    }

}
