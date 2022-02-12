package net.pixlies.core.listeners.moderation;

import net.kyori.adventure.text.Component;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class BanListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        User user = User.get(event.getPlayer().getUniqueId());
        if (!user.isBanned()) return;

        Punishment punishment = user.getBan();

        String banMessage = Lang.BAN_MESSAGE.get(event.getPlayer())
                .replace("%REASON%", punishment.getReason())
                .replace("%BAN_ID%", punishment.getID());

        if (punishment.getUntil() == 0) {
            banMessage = banMessage.replace("%DURATION%", "§4§lPERMANENT!");
        } else {
            PrettyTime prettyTime = new PrettyTime();
            banMessage = banMessage.replace("%DURATION%", prettyTime.format(new Date(punishment.getUntil())));
        }

        Lang.BANNED_PLAYER_TRIED_TO_JOIN.broadcastPermission("pixlies.moderation.logs", "%PLAYER%;", event.getPlayer().getName());
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Component.text(banMessage));
    }

}