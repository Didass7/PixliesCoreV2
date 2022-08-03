package net.pixlies.core.runnables.impl;

import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.FreezeHandler;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.runnables.PixliesRunnable;

/**
 * Edit the actionbar state.
 * @author Dynmie
 */
public class ActionBarRunnable extends PixliesRunnable {

    private static final Main instance = Main.getInstance();
    private final FreezeHandler freezeHandler = instance.getHandlerManager().getHandler(FreezeHandler.class);

    public ActionBarRunnable() {
        super(false, 30, 10);
    }

    @Override
    public void run() {
        Main.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            User user = User.get(player.getUniqueId());
            if (freezeHandler.isFrozen(player.getUniqueId())) {
                player.sendActionBar(Component.text(Lang.MODERATION_FREEZE_ACTIONBAR.get(player)));
                return;
            }
            if (user.isVanished()) {
                if (user.isInStaffMode()) return;
                player.sendActionBar(Component.text(Lang.MODERATION_VANISH_NO_ONE_CAN_SEE.get(player)));
            }
        });
    }

}
