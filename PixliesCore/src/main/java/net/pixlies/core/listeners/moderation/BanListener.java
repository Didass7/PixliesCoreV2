package net.pixlies.core.listeners.moderation;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.database.redis.RedisManager;
import net.pixlies.core.database.redis.RedisMessageReceiveEvent;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import net.pixlies.core.utils.json.JsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
                .replace("%EXECUTOR%", punishment.getPunisherName())
                .replace("%REASON%", punishment.getReason())
                .replace("%ID%", punishment.getPunishmentId());

        if (punishment.getUntil() == 0) {
            banMessage = banMessage.replace("%DURATION%", "§4§lPERMANENT!");
        } else {
            PrettyTime prettyTime = new PrettyTime();
            banMessage = banMessage.replace("%DURATION%", prettyTime.format(new Date(punishment.getUntil())));
        }

        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Component.text(banMessage));

        RedisManager.sendRequest("BanSpy", new JsonBuilder()
                .addProperty("playerName", event.getPlayer().getName())
                .addProperty("serverName", Main.getInstance().getDisplayName())
                .toJsonObject());
    }

    @EventHandler
    public void onReceive(RedisMessageReceiveEvent event) {
        if (!event.getIdentifier().equals("BanSpy")) return;

        JsonObject data = event.getData();

        String playerName = data.get("playerName").getAsString();
        String serverName = data.get("serverName").getAsString();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            User msgUser = User.get(p.getUniqueId());
            if (msgUser.isViewBannedJoins() && p.hasPermission("pixlies.moderation.banspy")) {
                Lang.BANNED_PLAYER_TRIED_TO_JOIN.send(p, "%PLAYER%;" + playerName, "%SERVER%;" + serverName);
            }
        }

    }

}