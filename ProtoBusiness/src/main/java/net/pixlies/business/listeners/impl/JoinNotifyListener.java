package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinNotifyListener implements Listener {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        instance.getServer().getScheduler().runTaskLater(
                instance,
                () -> MarketLang.MARKET_LOGON.send(player),
                3 * 20L
        );
        MarketProfile profile = MarketProfile.get(player.getUniqueId());
        profile.setHasJoinedBefore(true);
    }
}
