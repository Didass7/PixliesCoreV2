package net.pixlies.core.listeners.moderation;

import net.kyori.adventure.text.Component;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class BlackListListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        User user = User.get(event.getUniqueId());

        if (!user.isBlacklisted()) return;
        Punishment punishment = user.getBlacklist();

        String message = Lang.BLACKLIST_MESSAGE.get()
                .replace("%REASON%", punishment.getReason())
                .replace("%BAN_ID%", punishment.getID());

        Lang.BANNED_PLAYER_TRIED_TO_JOIN.broadcast("pixlies.moderation.logs", "%PLAYER%;" + event.getName());
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Component.text(message));
    }

}
