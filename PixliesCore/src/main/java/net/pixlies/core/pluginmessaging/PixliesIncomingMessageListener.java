package net.pixlies.core.pluginmessaging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.pixlies.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

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
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> onReceive(channel, player, message));
        } else {
            onReceive(channel, player, message);
        }
    }

    /**
     * @see PluginMessageListener#onPluginMessageReceived(String, Player, byte[])
     */
    public abstract void onReceive(@NotNull String channel, @NotNull Player player, byte[] message);

}
