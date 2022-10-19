package net.pixlies.core.listeners.moderation;

import com.google.gson.JsonObject;
import net.pixlies.core.Main;
import net.pixlies.core.database.redis.RedisManager;
import net.pixlies.core.database.redis.RedisMessageReceiveEvent;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.events.impl.moderation.UserKickedEvent;
import net.pixlies.core.events.impl.moderation.UserPunishedEvent;
import net.pixlies.core.events.impl.moderation.UserUnpunishedEvent;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.PlayerUtils;
import net.pixlies.core.utils.json.JsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.UUID;

public class PunishmentListener implements Listener {

    private static final Main instance = Main.getInstance();

    @SuppressWarnings("deprecation") // Legacy color formatting / legacy hex support
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        User user = User.getActiveUser(event.getUniqueId());

        if (!user.isPunishmentsLoaded()) {
            user.loadPunishments();
        }

        // BLACKLIST
        Punishment blacklist = user.getActiveBlacklist();
        if (blacklist != null) {
            final String punisherName;
            if (blacklist.isPunishedByConsole()) {
                punisherName = instance.getServer().getConsoleSender().getName();
            } else {
                User punisher = getLoadUser(event, blacklist.getPunishedBy());
                punisherName = punisher.getCurrentUsername();
            }

            String message = Lang.BLACKLIST_MESSAGE.get(user.getLang())
                    .replace("%EXECUTOR%", punisherName)
                    .replace("%REASON%", blacklist.getReason())
                    .replace("%ID%", blacklist.getPunishedBy().toString());

            instance.getMongoManager().getUserCache().remove(event.getUniqueId());
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);

            return;
        }

        // BAN
        Punishment ban = user.getActiveBan();
        if (ban != null) {
            final String punisherName;
            if (ban.isPunishedByConsole()) {
                punisherName = instance.getServer().getConsoleSender().getName();
            } else {
                User punisher = getLoadUser(event, ban.getPunishedBy());
                punisherName = punisher.getCurrentUsername();
            }

            String message = Lang.BAN_MESSAGE.get(user.getLang())
                    .replace("%EXECUTOR%", punisherName)
                    .replace("%REASON%", ban.getReason())
                    .replace("%ID%", ban.getPunishmentId().toString());

            if (ban.getDuration() == 0) {
                message = message.replace("%DURATION%", "§4§lPERMANENT!");
            } else {
                PrettyTime prettyTime = new PrettyTime();
                message = message.replace("%DURATION%", prettyTime.format(new Date(ban.getDuration())));
            }

            instance.getMongoManager().getUserCache().remove(event.getUniqueId());
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);

            RedisManager.sendRequest("BanSpy", new JsonBuilder()
                    .addProperty("playerName", event.getName())
                    .addProperty("serverName", Main.getInstance().getDisplayName())
                    .toJsonObject());
        }

    }

    @EventHandler
    public void onReceive(RedisMessageReceiveEvent event) {
        JsonObject data = event.getData();

        switch (event.getIdentifier()) {

            // BAN SPY
            case "BanSpy" -> {
                String playerName = data.get("playerName").getAsString();
                String serverName = data.get("serverName").getAsString();

                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    User msgUser = User.get(p.getUniqueId());
                    if (msgUser.isViewBannedJoins() && p.hasPermission("pixlies.moderation.banspy")) {
                        Lang.BANNED_PLAYER_TRIED_TO_JOIN.send(p, "%PLAYER%;" + playerName, "%SERVER%;" + serverName);
                    }
                }
            }

            // USER KICK
            case "UserKickBroadcast" -> {
                String name = data.get("name").getAsString();
                String executor = data.get("executor").getAsString();
                String reason = data.get("reason").getAsString();
                boolean silent = data.get("silent").getAsBoolean();

                if (silent) {
                    Lang.PLAYER_KICKED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason);
                } else {
                    Lang.PLAYER_KICKED.broadcast("%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason);
                }
            }
            
            case "UserMuteBroadcast" -> {
                String name = data.get("name").getAsString();
                String executor = data.get("executor").getAsString();
                String reason = data.get("reason").getAsString();
                boolean silent = data.get("silent").getAsBoolean();

                if (silent) {
                    Lang.PLAYER_PERMANENTLY_MUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason);
                } else {
                    Lang.PLAYER_PERMANENTLY_MUTED.broadcast("%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason);
                }
            }

            case "UserTempMuteBroadcast" -> {
                String name = data.get("name").getAsString();
                String executor = data.get("executor").getAsString();
                String reason = data.get("reason").getAsString();
                long until = data.get("until").getAsLong();
                boolean silent = data.get("silent").getAsBoolean();

                if (silent)
                    Lang.PLAYER_TEMPORARILY_MUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(until)));
                else
                    Lang.PLAYER_TEMPORARILY_MUTED.broadcast("%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(until)));
            }

            case "UserUnmuteBroadcast" -> {
                String name = data.get("name").getAsString();
                String executor = data.get("executor").getAsString();
                boolean silent = data.get("silent").getAsBoolean();

                if (silent) {
                    Lang.PLAYER_UNMUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + name, "%EXECUTOR%;" + executor);
                } else {
                    Lang.PLAYER_UNMUTED.broadcast("%PLAYER%;" + name, "%EXECUTOR%;" + executor);
                }
            }

            case "UserBanBroadcast" -> {
                String name = data.get("name").getAsString();
                String executor = data.get("executor").getAsString();
                String reason = data.get("reason").getAsString();
                boolean silent = data.get("silent").getAsBoolean();

                if (silent) {
                    Lang.PLAYER_PERMANENTLY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason);
                } else {
                    Lang.PLAYER_PERMANENTLY_BANNED.broadcast("%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason);               }
            }

            case "UserTempBanBroadcast" -> {
                String name = data.get("name").getAsString();
                String executor = data.get("executor").getAsString();
                String reason = data.get("reason").getAsString();
                long until = data.get("until").getAsLong();
                boolean silent = data.get("silent").getAsBoolean();

                if (silent)
                    Lang.PLAYER_TEMPORARILY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(until)));
                else
                    Lang.PLAYER_TEMPORARILY_BANNED.broadcast("%PLAYER%;" + name, "%EXECUTOR%;" + executor, "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(until)));
            }

            case "UserUnbanBroadcast" -> {
                String name = data.get("name").getAsString();
                String executor = data.get("executor").getAsString();
                boolean silent = data.get("silent").getAsBoolean();

                if (silent) {
                    Lang.PLAYER_UNBANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + name, "%EXECUTOR%;" + executor);
                } else {
                    Lang.PLAYER_UNBANNED.broadcast("%PLAYER%;" + name, "%EXECUTOR%;" + executor);
                }
            }

        }
    }

    @EventHandler
    public void onUserPunish(UserPunishedEvent event) {
        Punishment punishment = event.getPunishment();

        User user = event.getUser();
        CommandSender executor = event.getExecutor();
        String reason = punishment.getReason();

        switch (punishment.getType()) {
            case MUTE -> {
                // PERM MUTE
                if (punishment.isPermanent()) {
                    RedisManager.sendRequest("UserMuteBroadcast", new JsonBuilder()
                            .addProperty("name", user.getCurrentUsername())
                            .addProperty("executor", executor.getName())
                            .addProperty("reason", reason)
                            .addProperty("silent", event.isSilent())
                            .toJsonObject());
                    break;
                }

                // TEMP MUTE
                RedisManager.sendRequest("UserTempMuteBroadcast", new JsonBuilder()
                        .addProperty("name", user.getCurrentUsername())
                        .addProperty("executor", executor.getName())
                        .addProperty("reason", reason)
                        .addProperty("until", punishment.getUntil())
                        .addProperty("silent", event.isSilent())
                        .toJsonObject());
            }

            case BAN -> {
                // PERM MUTE
                if (punishment.isPermanent()) {
                    RedisManager.sendRequest("UserBanBroadcast", new JsonBuilder()
                            .addProperty("name", user.getCurrentUsername())
                            .addProperty("executor", executor.getName())
                            .addProperty("reason", reason)
                            .addProperty("silent", event.isSilent())
                            .toJsonObject());
                    break;
                }

                // TEMP MUTE
                RedisManager.sendRequest("UserTempBanBroadcast", new JsonBuilder()
                        .addProperty("name", user.getCurrentUsername())
                        .addProperty("executor", executor.getName())
                        .addProperty("reason", reason)
                        .addProperty("until", punishment.getUntil())
                        .addProperty("silent", event.isSilent())
                        .toJsonObject());
            }
        }

    }

    @EventHandler
    public void onUserUnpunished(UserUnpunishedEvent event) {
        User user = event.getUser();
        CommandSender executor = event.getExecutor();

        switch (event.getType()) {
            case MUTE -> RedisManager.sendRequest("UserUnmuteBroadcast", new JsonBuilder()
                    .addProperty("name", user.getCurrentUsername())
                    .addProperty("executor", executor.getName())
                    .addProperty("silent", event.isSilent())
                    .toJsonObject());

            case BAN -> RedisManager.sendRequest("UserUnbanBroadcast", new JsonBuilder()
                    .addProperty("name", user.getCurrentUsername())
                    .addProperty("executor", executor.getName())
                    .addProperty("silent", event.isSilent())
                    .toJsonObject());

            case BLACKLIST -> RedisManager.sendRequest("UserUnblacklistBroadcast", new JsonBuilder()
                    .addProperty("name", user.getCurrentUsername())
                    .addProperty("executor", executor.getName())
                    .addProperty("silent", event.isSilent())
                    .toJsonObject());
        }
    }

    @EventHandler
    @SuppressWarnings("deprecation") // legacy stuff
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (user.isMuted() && !player.hasPermission("pixlies.moderation.mute.exempt")) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                User msgUser = User.get(p.getUniqueId());
                if (msgUser.isViewMutedChat() && p.hasPermission("pixlies.moderation.mutespy")) {
                    Lang.MUTED_PLAYER_TRIED_TO_TALK.send(p,
                            "%PLAYER%;" + player.getName(),
                            "%MESSAGE%;" + event.getMessage());
                }
            }

            Lang.MUTE_MESSAGE.send(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onUserKick(UserKickedEvent event) {
        RedisManager.sendRequest("UserKickBroadcast", new JsonBuilder()
                .addProperty("name", event.getPlayer().getName())
                .addProperty("executor", event.getExecutor().getName())
                .addProperty("reason", event.getReason())
                .addProperty("silent", event.isSilent())
                .toJsonObject());
    }

    private static User getLoadUser(AsyncPlayerPreLoginEvent event, UUID uuid) {
        User punisher = User.getActiveUser(uuid);
        if (punisher.exists() || !punisher.isLoaded() && !punisher.isBasicLoaded()) {
            try {
                punisher.loadBasic();
            } catch (Exception e) {
                e.printStackTrace();
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.format("&cFailed to load punishments for " + event.getName()));
            }
        }
        return punisher;
    }

}