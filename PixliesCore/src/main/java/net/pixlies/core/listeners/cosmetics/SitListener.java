package net.pixlies.core.listeners.cosmetics;

import net.pixlies.core.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.spigotmc.event.entity.EntityDismountEvent;

public class SitListener implements Listener {

    private final Main instance = Main.getInstance();

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (!(event.getDismounted() instanceof Arrow arrow)) return;
        if (!(event.getEntity() instanceof Player player)) return;
        NamespacedKey key = new NamespacedKey(instance, "chair");
        String value = arrow.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (value == null) return;
        if (value.equals(player.getUniqueId().toString()))
            arrow.remove();
    }

}
