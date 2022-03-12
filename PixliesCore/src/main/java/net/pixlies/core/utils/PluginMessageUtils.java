package net.pixlies.core.utils;

import net.pixlies.core.Main;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class PluginMessageUtils {

    private PluginMessageUtils() {}

    /**
     * Easy way to send a plugin message
     */
    public static void sendMessage(Player player, String channel, DataOutput output) {
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
