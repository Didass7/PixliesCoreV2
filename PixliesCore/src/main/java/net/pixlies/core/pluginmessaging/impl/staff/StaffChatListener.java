package net.pixlies.core.pluginmessaging.impl.staff;

import net.pixlies.core.localization.Lang;
import net.pixlies.core.pluginmessaging.PixliesIncomingMessageListener;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class StaffChatListener extends PixliesIncomingMessageListener {

    public StaffChatListener() {
        super("pixlies:staffchat");
    }

    @Override
    public void onReceive(@NotNull String channel, @NotNull Player player, DataInputStream stream) throws IOException {

        String head = stream.readUTF();
        if (!head.equals("staffchat")) return;

        String name = stream.readUTF();
        String m = stream.readUTF(); // message

        Lang.STAFFCHAT_FORMAT.broadcastPermission("pixlies.staff.staffchat", "%PLAYER%;" + name, "%MESSAGE%;" + m);

    }

}
