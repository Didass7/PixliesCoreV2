package net.pixlies.core.handlers.impl;

import net.pixlies.core.Main;
import net.pixlies.core.database.redis.RedisManager;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.json.JsonBuilder;
import org.bukkit.entity.Player;

/**
 * Handles the sending of staff messages
 * @author vyketype
 */
public class StaffChatHandler implements Handler {

    public static final String STAFF_CHAT_IDENTIFIER = "StaffChat";

    /**
     * Sends a staff message across all servers
     * @param message message to send
     */
    public void sendStaffChat(Player player, String message) {

        RedisManager.sendRequest(STAFF_CHAT_IDENTIFIER, new JsonBuilder()
                .addProperty("senderName", player.getName())
                .addProperty("serverName", Main.getInstance().getConfig().getString("server.name", ""))
                .addProperty("message", message)
                .toJsonObject());

    }

}
