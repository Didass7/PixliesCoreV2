package net.pixlies.core.listeners.moderation;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.ModerationHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final ModerationHandler moderationHandler = instance.getHandlerManager().getHandler(ModerationHandler.class);

    @EventHandler
    public void onAttack (EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        if (attacker instanceof Player && victim instanceof Player) {
            if (!moderationHandler.isPvpOn() && !attacker.hasPermission("pixlies.moderation.togglepvp.bypass")) {
                Lang.PVP_OFF_PLAYER_TRIED_TO_PVP.send(attacker);
                event.setCancelled(true);
            }
        }
    }

}
