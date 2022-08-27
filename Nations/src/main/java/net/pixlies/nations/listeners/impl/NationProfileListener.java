package net.pixlies.nations.listeners.impl;

import net.pixlies.core.utils.CC;
import net.pixlies.nations.Nations;
import net.pixlies.nations.interfaces.NationProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NationProfileListener implements Listener {

    private static final Nations instance = Nations.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        // REMOVE OLD PROFILE FROM CACHE TO UPDATE INFO
        instance.getMongoManager().getProfileCache().remove(event.getUniqueId());

        // ADD AND LOAD PROFILE
        NationProfile profile = NationProfile.get(event.getUniqueId());
        profile.load();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        // GET PROFILE
        NationProfile profile = NationProfile.get(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        instance.getServer().getScheduler().runTaskLater(instance, () -> {
            // CANCEL IF PLAYER OFFLINE
            if (!player.isOnline()) return;

            // GET PROFILE
            NationProfile profile = NationProfile.get(event.getPlayer().getUniqueId());

            // CHECK IF PROFILE IS LOADED WHEN IT SHOULD BE LOADED
            if (!profile.isLoaded()) {
                player.kickPlayer(CC.format("&cYour NationProfile has failed to load.\nIf this error persists, please contact a staff member."));
                return;
            }

            // SET LAST LOGIN TO NATIONS
            profile.setLastLogin(System.currentTimeMillis());

        }, 70); // 3.5s
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // REMOVE FROM CACHE
        Player player = event.getPlayer();
        instance.getMongoManager().getProfileCache().remove(player.getUniqueId());
    }

}
