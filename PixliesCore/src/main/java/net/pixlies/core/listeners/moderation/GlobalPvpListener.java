package net.pixlies.core.listeners.moderation;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.GlobalPvpHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GlobalPvpListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final GlobalPvpHandler globalPvpHandler = instance.getHandlerManager().getHandler(GlobalPvpHandler.class);

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        if (!(attacker instanceof Player)) return;
        if (!(victim instanceof Player)) return;

        event.setCancelled(true);

    }

}
