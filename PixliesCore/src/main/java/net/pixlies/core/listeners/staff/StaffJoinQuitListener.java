package net.pixlies.core.listeners.staff;

import com.google.gson.JsonObject;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.database.redis.RedisManager;
import net.pixlies.core.database.redis.RedisMessageReceiveEvent;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.json.JsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffJoinQuitListener implements Listener {

    private static final Main instance = Main.getInstance();
    private static final Config config = instance.getConfig();

    @EventHandler
    public void onReceive(RedisMessageReceiveEvent event) {
        if (!(event.getIdentifier().equals("StaffServerJoin") || event.getIdentifier().equals("StaffServerQuit"))) {
            return;
        }

        JsonObject jsonObject = event.getData();

        String playerName = jsonObject.get("playerName").getAsString();
        String serverName = jsonObject.get("serverName").getAsString();

        switch (event.getIdentifier()) {
            case "StaffServerJoin" -> Lang.STAFF_SERVER_JOIN_FORMAT.broadcastPermission("pixlies.staff.joinquit",
                    "%PLAYER%;" + playerName,
                    "%SERVER%;" + serverName);
            case "StaffServerQuit" -> Lang.STAFF_SERVER_QUIT_FORMAT.broadcastPermission("pixlies.staff.joinquit",
                    "%PLAYER%;" + playerName,
                    "%SERVER%;" + serverName);
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (!player.hasPermission("pixlies.staff.joinquit")) return;

        instance.getServer().getScheduler().runTaskLater(instance, () -> RedisManager.sendRequest("StaffServerJoin", new JsonBuilder()
                .addProperty("playerName", player.getName())
                .addProperty("serverName", instance.getServerName())
                .toJsonObject()), 20);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        if (!player.hasPermission("pixlies.staff.joinquit")) return;

        instance.getServer().getScheduler().runTaskLater(instance, () -> RedisManager.sendRequest("StaffServerQuit", new JsonBuilder()
                .addProperty("playerName", player.getName())
                .addProperty("serverName", instance.getServerName())
                .toJsonObject()), 15);

    }

}
