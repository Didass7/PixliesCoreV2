package net.pixlies.lobby.listeners.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.utils.LobbyUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private static final Lobby instance = Lobby.getInstance();
    private static final ModuleConfig config = instance.getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        String joinMessage = CC.format(PlaceholderAPI.setPlaceholders(player, config.getString("join_leave.join_message", "")));
        if (!joinMessage.isEmpty()) {
            instance.getServer().getScheduler().runTaskLater(instance, () -> player.sendMessage(joinMessage), 5);
        }

        LobbyUtils.resetPlayer(player, true);

    }

}
