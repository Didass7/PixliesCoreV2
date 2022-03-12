package net.pixlies.core.pluginmessaging.impl.staff;

import net.pixlies.core.localization.Lang;
import net.pixlies.core.pluginmessaging.PixliesIncomingMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class StaffChatListener extends PixliesIncomingMessageListener {

    public StaffChatListener() {
        super("pixlies:staffchat");
    }

    @Override
    public void onReceive(@NotNull String channel, @NotNull Player player, byte[] message) {

        ByteArrayInputStream stream = new ByteArrayInputStream(message);
        DataInputStream in = new DataInputStream(stream);

        try {

            String head = in.readUTF();
            if (!head.equals("staffchat")) return;

            String u = in.readUTF();
            UUID uuid = UUID.fromString(u);

            String m = in.readUTF(); // message

            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);

            Lang.STAFFCHAT_FORMAT.broadcastPermission("pixlies.staff.staffchat", "%PLAYER%;" + p.getName(), "%MESSAGE%;" + m);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
