package net.pixlies.nations.listeners.impl;

import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.NationProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NationProfileListener implements Listener {

    private static final Nations instance = Nations.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // REMOVE OLD PROFILE FROM CACHE TO UPDATE INFO
        instance.getMongoManager().getProfileCache().remove(event.getPlayer().getUniqueId());

        // ADD PROFILE
        NationProfile profile = NationProfile.get(event.getPlayer().getUniqueId());

        // SET LAST LOGIN TO NATIONS
        profile.setLastLogin(System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // REMOVE FROM CACHE
        Player player = event.getPlayer();
        instance.getMongoManager().getProfileCache().remove(player.getUniqueId());
    }

}
