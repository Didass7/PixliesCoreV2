package net.pixlies.core.listeners.moderation;

import net.kyori.adventure.text.Component;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class BlackListListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());

        if (!user.isBlacklisted()) return;
        Punishment punishment = user.getBlacklist();
        if (punishment == null) return;

        String message = Lang.BLACKLIST_MESSAGE.get(player)
                .replace("%EXECUTOR%", punishment.getPunisher())
                .replace("%REASON%", punishment.getReason())
                .replace("%BAN_ID%", punishment.getID());

        Lang.BANNED_PLAYER_TRIED_TO_JOIN.broadcast("pixlies.moderation.logs", "%PLAYER%;" + player.getName());
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Component.text(message));
    }

}
