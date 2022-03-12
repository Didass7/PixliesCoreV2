package net.pixlies.core.handlers.impl;

import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.PluginMessageUtils;
import org.bukkit.entity.Player;

/**
 * Handles the sending of staff messages
 * @author vPrototype_
 */
public class StaffChatHandler implements Handler {

    /**
     * Sends a staff message across all servers
     * @param message message to send
     */
    public void sendStaffChat(Player player, String message) {

        PluginMessageUtils.sendMessage(player, "pixlies:staffchat", out -> {
            out.writeUTF("staffchat");
            out.writeUTF(player.getUniqueId().toString());
            out.writeUTF(message);
        });

    }

}
