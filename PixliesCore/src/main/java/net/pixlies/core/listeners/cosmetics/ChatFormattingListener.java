package net.pixlies.core.listeners.cosmetics;

import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.PixlieMojiHandler;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormattingListener implements Listener {

    private final Config config = Main.getInstance().getConfig();
    @EventHandler
    public void onChat(@SuppressWarnings("deprecation") AsyncPlayerChatEvent event) { // legacy system

        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        String message = ChatUtils.formatByPerm(player, event.getMessage());

        // SET MESSAGE
        event.setMessage(PixlieMojiHandler.replaceEmojis(message));

        // SET CHAT FORMATTING
        if (config.getBoolean("chat.formatting", false)) {
            event.setFormat(CC.format(CC.setPlaceholders(player, config.getString("chat.format", "%pixlies_chatprefix%%nickname%%pixlies_chatsuffix%&7: &f%message%")
                    .replace("%player%", player.getName())
                    .replace("%nickname%", user.getNickName())
                    .replace("%message%", message))));
        }

    }

}
