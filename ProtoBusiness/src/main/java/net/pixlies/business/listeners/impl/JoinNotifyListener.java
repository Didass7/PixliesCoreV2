package net.pixlies.business.listeners.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinNotifyListener implements Listener {
    private static final ProtoBusiness instance = ProtoBusiness.getInstance();
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MarketLang.MARKET_LOGON.send(player);
    }
}
