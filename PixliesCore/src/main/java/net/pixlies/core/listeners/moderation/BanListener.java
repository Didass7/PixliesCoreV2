package net.pixlies.core.listeners.moderation;

import net.kyori.adventure.text.Component;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class BanListener implements Listener {

    private static final String BAN_BROADCAST_PERMISSION = "earth.ban.broadcast";

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        User user = User.get(event.getPlayer().getUniqueId());
        if (user.getCurrentPunishments().containsKey("ban")) {
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

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Component.text(banMessage));
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.hasPermission(BAN_BROADCAST_PERMISSION)) {
                    Lang.BANNED_PLAYER_TRIED_TO_JOIN.send(p, "%PLAYER%;", event.getPlayer().getName());
                }
            }
        }
    }

}
