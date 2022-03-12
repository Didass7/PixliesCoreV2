package net.pixlies.core.pluginmessaging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.pixlies.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Abstract class for your plugin messaging needs
 * @author Dynmie
 */
@Getter
@RequiredArgsConstructor
public abstract class PixliesIncomingMessageListener implements PluginMessageListener {

    private final String channel;
    @Setter private boolean async = false;

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                try {
                    onReceive(channel, player, message);
                    ByteArrayInputStream stream = new ByteArrayInputStream(message);
                    DataInputStream in = new DataInputStream(stream);
                    onReceive(channel, player, in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                onReceive(channel, player, message);
                ByteArrayInputStream stream = new ByteArrayInputStream(message);
                DataInputStream in = new DataInputStream(stream);
                onReceive(channel, player, in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @see PluginMessageListener#onPluginMessageReceived(String, Player, byte[])
     */
    public void onReceive(@NotNull String channel, @NotNull Player player, byte[] message) throws IOException {

    }

    /**
     * A less complex version of the below see.
     * @see PluginMessageListener#onPluginMessageReceived(String, Player, byte[])
     */
    public void onReceive(@NotNull String channel, @NotNull Player player, @NotNull DataInputStream stream) throws IOException {

    }

}
