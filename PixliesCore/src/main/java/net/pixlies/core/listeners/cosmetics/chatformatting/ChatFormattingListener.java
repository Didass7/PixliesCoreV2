package net.pixlies.core.listeners.cosmetics.chatformatting;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.Emojis;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormattingListener implements Listener {

    private final Config config = Main.getInstance().getConfig();
    private final FormatRenderer renderer = new FormatRenderer();

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        Player player = event.getPlayer();
        String message = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());

        // IF PLAYER CAN USE CHAT COLOR
        if (player.hasPermission("pixlies.chat.format")) {
            message = CC.format(message);
        }

        // SET MESSAGE
        event.message(Component.text(Emojis.replaceEmojis(message)));

        // SET CHAT FORMATTING
        if (config.getBoolean("chat.formatting", false)) {
            event.renderer(renderer);
        }

    }

}
