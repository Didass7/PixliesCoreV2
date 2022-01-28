package net.pixlies.core.listeners.cosmetics;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.CosmeticsHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final CosmeticsHandler cosmeticsHandler = instance.getHandlerManager().getHandler(CosmeticsHandler.class);

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String name = player.getName();
        if (cosmeticsHandler.isSuicidalDeath()) {
            event.setDeathMessage(null);
            Lang.COSMETICS_ANNOUNCE_PLAYER_SUICIDE.broadcast("%PLAYER%;" + name);
        } else {
            assert event.getDeathMessage() != null;
            event.setDeathMessage("§4❣ §7" + (event.getDeathMessage()).replaceAll(name, "§6" + name + "§7"));
        }
        cosmeticsHandler.setSuicidalDeath(false);
    }

}
