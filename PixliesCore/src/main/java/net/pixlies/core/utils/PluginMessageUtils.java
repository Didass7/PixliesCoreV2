package net.pixlies.core.utils;

import net.pixlies.core.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class PluginMessageUtils {

    private PluginMessageUtils() {}

    /**
     * Easy way to send a plugin message
     * @param player the player to send the message through.
     * @param channel the channel to send the message through.
     * @param output use a lambda
     */
    public static void sendMessage(@NotNull Player player, @NotNull String channel, @NotNull DataOutput output) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            output.execute(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(Main.getInstance(), channel, stream.toByteArray());

    }

    public interface DataOutput {

        /**
         * Stream is caught with IOException
         * @param stream the stream
         */
        void execute(DataOutputStream stream) throws IOException;

    }

}
