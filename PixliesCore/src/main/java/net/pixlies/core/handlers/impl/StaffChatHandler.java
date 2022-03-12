package net.pixlies.core.handlers.impl;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.Handler;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF("staffchat");
            out.writeUTF(player.getName());
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(Main.getInstance(), "pixlies:staffchat", stream.toByteArray());

    }

}
