package net.pixlies.core.pluginmessaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public abstract class PixliesIncomingMessageListener implements PluginMessageListener {

    private final String channel;
    private boolean async;

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> onReceive(channel, player, message));
        } else {
            onReceive(channel, player, message);
        }
    }

    public abstract void onReceive(@NotNull String channel, @NotNull Player player, byte[] message);

}
