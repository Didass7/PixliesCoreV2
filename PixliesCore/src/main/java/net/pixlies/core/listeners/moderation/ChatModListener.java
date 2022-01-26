package net.pixlies.core.listeners.moderation;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.Emojis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.List;

public class ChatModListener implements Listener {

    private static final Main instance = Main.getInstance();
    private static final Config config = new Config(
            new File(instance.getDataFolder().getAbsolutePath() + "/config.yml"), "config.yml");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = String.valueOf(event.message());

        if (instance.isChatMuted() && !player.hasPermission("pixlies.moderation.bypass.mutechat")) {
            Lang.MUTE_MESSAGE.send(player);
            event.setCancelled(true);
            return;
        }

        if (!instance.isSwearFilter() && !player.hasPermission("pixlies.moderation.bypass.swearfilter")) {
            List<String> swearWords = config.getStringList("swearWords");
            for (String s : swearWords) {
                if (message.toLowerCase().contains(s)) {
                    Lang.PLAYER_TRIED_TO_SWEAR.send(player);
                    event.setCancelled(true);
                    return;
                }
            }
        }

        List<String> blockedWords = instance.getConfig().getStringList("blockedWords");
        for (String s : blockedWords) {
            if (message.contains(s) && !player.hasPermission("pixlies.moderation.bypass.blockedwords")) {
                Lang.PLAYER_TRIED_BLOCKED_WORD.send(player);
                event.setCancelled(true);
                return;
            }
        }

        event.message(Component.text(Emojis.replaceEmojis(String.valueOf(event.message()))));
    }

}
