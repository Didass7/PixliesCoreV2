package net.pixlies.core.listeners.cosmetics.chatformatting;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.handlers.impl.PixlieMojiHandler;
import net.pixlies.core.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

        // SET MESSAGE TODO
        event.message(Component.text(PixlieMojiHandler.replaceEmojis(message)));

        // SET CHAT FORMATTING
        if (config.getBoolean("chat.formatting", false)) {
            event.renderer(renderer);
        }

    }

}
