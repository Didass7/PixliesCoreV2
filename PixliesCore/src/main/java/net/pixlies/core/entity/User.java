package net.pixlies.core.entity;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.Main;
import net.pixlies.core.economy.Wallet;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import net.pixlies.core.moderation.PunishmentType;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class User {

    private static final Main instance = Main.getInstance();

    private static final Gson gson = new Gson();

    private UUID uuid;
    private long joined;
    private String discordId;
    private Map<String, Wallet> wallets;
    private List<String> knownUsernames;
    private List<UUID> blockedUsers;
    private Map<String, Object> stats;
    private Map<String, Punishment> currentPunishments;
    private String lang;

    public OfflinePlayer getAsOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public Punishment getMute() {
        if (!currentPunishments.containsKey("mute")) return null;
        Punishment punishment = currentPunishments.get("mute");
        if (punishment.getUntil() - System.currentTimeMillis() <= 0) {
            currentPunishments.remove("mute");
            return null;
        }
        return punishment;
    }

    public Punishment mute(String reason, CommandSender punisher, boolean silent) {
        UUID punisherUUID = punisher.getName().equalsIgnoreCase("console") ? UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670") : ((Player)punisher).getUniqueId();
        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.MUTE, punisherUUID, System.currentTimeMillis(), reason, 0);
        currentPunishments.put("mute", punishment);
        if (silent) {
            Lang.PLAYER_PERMANENTLY_MUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        } else {
            Lang.PLAYER_PERMANENTLY_MUTED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        }
        return punishment;
    }

    public Punishment tempMute(String reason, CommandSender punisher, long duration, boolean silent) {
        UUID punisherUUID = punisher.getName().equalsIgnoreCase("console") ? UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670") : ((Player)punisher).getUniqueId();
        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.MUTE, punisherUUID, System.currentTimeMillis(), reason, duration + System.currentTimeMillis());
        currentPunishments.put("mute", punishment);
        if (silent)
            Lang.PLAYER_TEMPORARILY_MUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        else
            Lang.PLAYER_TEMPORARILY_MUTED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        return punishment;
    }

    public Punishment getBan() {
        if (!currentPunishments.containsKey("ban")) return null;
        Punishment punishment = currentPunishments.get("ban");
        if (punishment.getUntil() - System.currentTimeMillis() <= 0) {
            currentPunishments.remove("ban");
            return null;
        }
        return punishment;
    }

    public Punishment ban(String reason, CommandSender punisher, boolean silent) {
        UUID punisherUUID = punisher.getName().equalsIgnoreCase("console") ? UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670") : ((Player)punisher).getUniqueId();
        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.BAN, punisherUUID, System.currentTimeMillis(), reason, 0);
        currentPunishments.put("ban", punishment);
        if (silent)
            Lang.PLAYER_PERMANENTLY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        else
            Lang.PLAYER_PERMANENTLY_BANNED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        return punishment;
    }

    public Punishment tempBan(String reason, CommandSender punisher, long duration, boolean silent) {
        UUID punisherUUID = punisher.getName().equalsIgnoreCase("console") ? UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670") : ((Player)punisher).getUniqueId();
        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.BAN, punisherUUID, System.currentTimeMillis(), reason, duration + System.currentTimeMillis());
        currentPunishments.put("ban", punishment);
        if (silent)
            Lang.PLAYER_TEMPORARILY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        else
            Lang.PLAYER_TEMPORARILY_BANNED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        return punishment;
    }

    public static User get(UUID uuid) {
        return instance.getDatabase().getUserCache().getOrDefault(uuid, getFromDatabase(uuid));
    }

    public static User getFromDatabase(UUID uuid) {
        Document profile = new Document("uniqueId", uuid.toString());
        Document found = instance.getDatabase().getUserCollection().find(profile).first();
        User data;
        if (found == null) {
            profile.append("joined", System.currentTimeMillis());
            profile.append("discordId", "NONE");
            profile.append("wallets", Wallet.mapAllForMongo(Wallet.getDefaultWallets()));
            profile.append("knownUsernames", new ArrayList<>());
            profile.append("blockedUsers", new ArrayList<>());
            profile.append("stats", new HashMap<>());
            profile.append("currentPunishments", Punishment.mapAllForMongo(new HashMap<>()));
            profile.append("lang", "ENG");

            instance.getDatabase().getUserCollection().insertOne(profile);

            data = new User(
                    uuid,
                    System.currentTimeMillis(),
                    "NONE",
                    Wallet.getDefaultWallets(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new HashMap<>(),
                    new HashMap<>(),
                    "ENG"
            );

            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Profile for " + uuid + " created in Database.");
        } else {
            data = new User(
                    UUID.fromString(found.getString("uuid")),
                    found.getLong("joined"),
                    found.getString("discordId"),
                    Wallet.getFromMongo((Map<String, Map<String, Object>>) found.get("wallets", Map.class)),
                    found.getList("knownUsernames", String.class),
                    found.getList("blockedUsers", String.class).stream().map(UUID::fromString).collect(Collectors.toList()),
                    found.get("stats", Map.class),
                    Punishment.getFromMongo((Map<String, Map<String, Object>>) found.get("currentPunishments")),
                    found.getString("lang")
            );
        }
        return data;
    }

    public void backup() {
        Document profile = new Document("uuid", uuid);
        Document found = instance.getDatabase().getUserCollection().find(profile).first();
        if (found == null) return;
        profile.append("joined", joined);
        profile.append("discordId", discordId);
        profile.append("wallets", Wallet.mapAllForMongo(wallets));
        profile.append("knownUsernames", knownUsernames);
        profile.append("blockedUsers", blockedUsers.stream()
                .map(UUID::toString)
                .collect(Collectors.toList())
        );
        profile.append("stats", gson.toJson(stats));
        profile.append("currentPunishments", Punishment.mapAllForMongo(currentPunishments));
        profile.append("lang", lang);
        instance.getDatabase().getUserCollection().replaceOne(found, profile);
    }

    public void save() {
        instance.getDatabase().getUserCache().put(uuid, this);
    }
    
}
