package net.pixlies.core.entity;

import com.google.gson.Gson;
import com.mongodb.lang.Nullable;
import dev.morphia.annotations.*;
import dev.morphia.query.experimental.filters.Filters;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.economy.Wallet;
import net.pixlies.core.entity.data.PermissionProfile;
import net.pixlies.core.entity.data.UserPersonalization;
import net.pixlies.core.entity.data.UserSettings;
import net.pixlies.core.entity.data.UserStats;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import net.pixlies.core.moderation.PunishmentType;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.*;

@Data
@AllArgsConstructor
@Entity("users")
@Indexes(
        @Index(fields = { @Field("uuid")})
)
public class User {

    public static final Main instance = Main.getInstance();

    public static final Gson gson = new Gson();

    @Id private String uuid;
    private long joined;
    private String discordId;
    private String nickName;
    private Map<String, Wallet> wallets;
    private List<String> knownUsernames;
    private List<UUID> blockedUsers;
    private UserStats stats;
    private Map<String, Punishment> currentPunishments;
    private UserPersonalization personalization;
    private UserSettings settings;
    private String lang;
    private PermissionProfile permissionProfile;
    private Map<String, Object> extras;

    public OfflinePlayer getAsOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.getUniqueId());
    }

    public Punishment getMute() {
        if (!currentPunishments.containsKey("mute")) return null;
        Punishment punishment = currentPunishments.get("mute");
        if (punishment.getUntil() - System.currentTimeMillis() <= 0 && punishment.getUntil() != 0) {
            currentPunishments.remove("mute");
            return null;
        }
        return punishment;
    }

    public boolean isMuted() {
        return getMute() != null;
    }

    public @NotNull Punishment mute(@Nullable String reason, @NotNull CommandSender punisher, boolean silent) {

        if (isMuted()) return getMute();
        UUID punisherUUID = punisher instanceof Player player ? player.getUniqueId() : UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

        String newReason = reason;

        if (newReason == null || newReason.isEmpty()) {
            newReason = instance.getConfig().getString("moderation.defaultReason", "No reason given");
        }

        Punishment punishment = new Punishment(
                UUID.randomUUID().toString(),
                PunishmentType.MUTE.name(),
                punisherUUID.toString(),
                System.currentTimeMillis(),
                newReason,
                0
        );

        currentPunishments.put("mute", punishment);
        if (silent) {
            Lang.PLAYER_PERMANENTLY_MUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        } else {
            Lang.PLAYER_PERMANENTLY_MUTED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + newReason);
        }
        save();
        return punishment;
    }

    public void unmute(@NotNull CommandSender sender, boolean silent) {
        if (!isMuted()) return;
        currentPunishments.remove("mute");
        if (silent) {
            Lang.PLAYER_UNMUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName());
        } else {
            Lang.PLAYER_UNMUTED.broadcast("%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName());
        }
        save();
    }

    public @NotNull Punishment tempMute(@Nullable String reason, @NotNull CommandSender punisher, long duration, boolean silent) {
        if (isMuted()) return getMute();
        UUID punisherUUID = punisher instanceof Player player ? player.getUniqueId() : UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

        String newReason = reason;

        if (newReason == null || newReason.isEmpty()) {
            newReason = instance.getConfig().getString("moderation.defaultReason", "No reason given");
        }

        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.MUTE.name(), punisherUUID.toString(), System.currentTimeMillis(), newReason, duration + System.currentTimeMillis());
        currentPunishments.put("mute", punishment);
        if (silent)
            Lang.PLAYER_TEMPORARILY_MUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        else
            Lang.PLAYER_TEMPORARILY_MUTED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + newReason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        save();
        return punishment;
    }

    public Punishment getBan() {
        if (!currentPunishments.containsKey("ban")) return null;
        Punishment punishment = currentPunishments.get("ban");
        if (punishment.getUntil() - System.currentTimeMillis() <= 0 && punishment.getUntil() != 0) {
            currentPunishments.remove("ban");
            return null;
        }
        return punishment;
    }

    public boolean isBanned() {
        return getBan() != null;
    }

    public @NotNull Punishment ban(@Nullable String reason, @NotNull CommandSender punisher, boolean silent) {
        if (isBanned()) return getBan();
        UUID punisherUUID = punisher instanceof Player player ? player.getUniqueId() : UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

        String newReason = reason;

        if (newReason == null || newReason.isEmpty()) {
            newReason = instance.getConfig().getString("moderation.defaultReason", "No reason given");
        }

        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.BAN.name(), punisherUUID.toString(), System.currentTimeMillis(), newReason, 0);
        currentPunishments.put("ban", punishment);
        if (silent)
            Lang.PLAYER_PERMANENTLY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        else
            Lang.PLAYER_PERMANENTLY_BANNED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + newReason);
        if (getAsOfflinePlayer().getPlayer() != null && getAsOfflinePlayer().isOnline()) {
            Player player = getAsOfflinePlayer().getPlayer();
            String message = Lang.BAN_MESSAGE.get(player)
                    .replace("%REASON%", punishment.getReason())
                    .replace("%BAN_ID%", punishment.getID())
                    .replace("%DURATION%", "§4§lPERMANENT!");

            player.kick(Component.text(message));
        }
        save();
        return punishment;
    }

    public @NotNull Punishment tempBan(@Nullable String reason, @NotNull CommandSender punisher, long duration, boolean silent) {
        if (isBanned()) return getBan();
        UUID punisherUUID = punisher instanceof Player player ? player.getUniqueId() : UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

        String newReason = reason;

        if (newReason == null || newReason.isEmpty()) {
            newReason = instance.getConfig().getString("moderation.defaultReason", "No reason given");
        }

        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.BAN.name(), punisherUUID.toString(), System.currentTimeMillis(), newReason, duration + System.currentTimeMillis());
        currentPunishments.put("ban", punishment);
        if (silent)
            Lang.PLAYER_TEMPORARILY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        else
            Lang.PLAYER_TEMPORARILY_BANNED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + newReason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));

        if (getAsOfflinePlayer().getPlayer() != null && getAsOfflinePlayer().isOnline()) {
            Player player = getAsOfflinePlayer().getPlayer();
            String message = Lang.BAN_MESSAGE.get(player)
                    .replace("%REASON%", punishment.getReason())
                    .replace("%BAN_ID%", punishment.getID())
                    .replace("%DURATION%", new PrettyTime().format(new Date(punishment.getUntil())));

            player.kick(Component.text(message));
        }
        save();
        return punishment;
    }

    public void unban(CommandSender sender, boolean silent) {
        if (!isBanned()) return;
        currentPunishments.remove("ban");
        if (silent) {
            Lang.PLAYER_UNBANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName());
        } else {
            Lang.PLAYER_UNBANNED.broadcast("%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName());
        }
        save();
    }

    public @Nullable Punishment getBlacklist() {
        if (!currentPunishments.containsKey("blacklist")) return null;
        return currentPunishments.get("blacklist");
    }

    public boolean isBlacklisted() {
        return currentPunishments.containsKey("blacklist");
    }

    /**
     * Blacklists a player because they did a horrible no-no.
     * @param reason the reason for the blacklist
     * @param punisher the punisher
     * @param silent if you want it broadcasted to staff or not
     * @return the punishment.
     */
    public @NotNull Punishment blacklist(@Nullable String reason, @NotNull CommandSender punisher, boolean silent) {
        if (isBlacklisted()) return Objects.requireNonNull(getBlacklist());

        UUID punisherUUID = punisher instanceof Player player ? player.getUniqueId() : UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

        String newReason = reason;

        if (newReason == null || newReason.isEmpty()) {
            newReason = instance.getConfig().getString("moderation.defaultReason", "No reason given");
        }


        Punishment punishment = new Punishment(UUID.randomUUID().toString(),
                PunishmentType.BLACKLIST.name(),
                punisherUUID.toString(),
                System.currentTimeMillis(),
                newReason,
                0
        );

        currentPunishments.put("blacklist", punishment);
        if (silent) {
            Lang.PLAYER_BLACKLISTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        } else {
            Lang.PLAYER_BLACKLISTED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + newReason);
        }
        if (getAsOfflinePlayer().getPlayer() != null && getAsOfflinePlayer().isOnline()) {
            Player player = getAsOfflinePlayer().getPlayer();
            String message = Lang.BLACKLIST_MESSAGE.get(player)
                    .replace("%REASON%", punishment.getReason())
                    .replace("%BAN_ID%", punishment.getID());
            player.kick(Component.text(message));
        }
        save();
        return punishment;
    }

    public void unblacklist(@NotNull CommandSender sender, boolean silent) {
        if (!isBlacklisted()) return;
        currentPunishments.remove("blacklist");
        if (silent) {
            Lang.PLAYER_UNBLACKLISTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName());
        } else {
            Lang.PLAYER_UNBLACKLISTED.broadcast("%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName());
        }
        save();
    }

    /**
     * Kicks the user with a reason.
     * @param reason the reason to kick
     * @param sender the punisher that punished the player
     * @param silent silently kick the player
     * @return true if success, false if failed
     */
    public boolean kick(@Nullable String reason, @NotNull CommandSender sender,  boolean silent) {
        String kickReason = reason;
        if (!getAsOfflinePlayer().isOnline()) return false;
        Player player = getAsOfflinePlayer().getPlayer();
        if (player == null)
            return false;
        if (player.hasPermission("pixlies.moderation.kick.exempt"))
            return false;
        if (reason == null || reason.isEmpty()) {
            kickReason = instance.getConfig().getString("moderation.defaultReason", "No reason given");
        }
        String kickMessage = Lang.KICK_MESSAGE.get(player);
        kickMessage = kickMessage
                .replace("%REASON%",  kickReason);
        player.kick(Component.text(kickMessage));
        if (silent) {
            Lang.PLAYER_KICKED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName(), "%REASON%;" + kickReason);
        } else {
            Lang.PLAYER_KICKED.broadcast("%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName(), "%REASON%;" + kickReason);
        }
        return true;
    }

    /**
     * Kicks the user with the default reason.
     * @return true if success, false if failed
     */
    public boolean kick(@NotNull CommandSender sender, boolean silent) {
        return this.kick(null, sender, silent);
    }

    public boolean hasNickName() {
        return nickName != null && !nickName.isEmpty();
    }

    public void setNickName(String name) {
        if (name.length() > 16 || name.isEmpty()) {
            throw new IllegalArgumentException("Illegal nickname. Current length: " + name.length());
        }
        nickName = name;
        save();
    }

    public void removeNickName() {
        nickName = null;
    }

    public String getNickName() {
        if (nickName == null || nickName.isEmpty()) return getAsOfflinePlayer().getName();
        return nickName;
    }

    public long getPlaytime() {
        return System.currentTimeMillis() - joined;
    }

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    public void backup() {
        try {
            instance.getDatabase().getDatastore().find(User.class).filter(Filters.gte("uuid", uuid)).delete();
        } catch (Exception ignored) { }

        instance.getDatabase().getDatastore().save(this);
    }

    public void save() {
        instance.getDatabase().getUserCache().remove(getUniqueId());
        instance.getDatabase().getUserCache().put(getUniqueId(), this);
    }

    // STATICS - it's not static abuse if you use it properly.

    public static @NotNull User get(UUID uuid) {
        return instance.getDatabase().getUserCache().getOrDefault(uuid, getFromDatabase(uuid));
    }

    public static @NotNull Collection<User> getAllUsers() {
        return instance.getDatabase().getUserCache().values();
    }

    public static @NotNull Collection<User> getOnlineUsers() {
        List<User> users = new ArrayList<>();
        for (User user : getAllUsers()) {
            if (!user.getAsOfflinePlayer().isOnline())
                continue;
            users.add(user);
        }
        return users;
    }

    public static @NotNull User getFromDatabase(UUID uuid) {
        User profile = instance.getDatabase().getDatastore().find(User.class).filter(Filters.gte("uuid", uuid.toString())).first();
        if (profile == null) {
            profile = new User(
                    uuid.toString(),
                    System.currentTimeMillis(),
                    "NONE",
                    "NONE",
                    Wallet.getDefaultWallets(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    UserStats.createNew(),
                    new HashMap<>(),
                    UserPersonalization.getDefaults(),
                    UserSettings.getDefaults(),
                    "ENG",
                    new PermissionProfile(new ArrayList<>(), new ArrayList<>()),
                    new HashMap<>()
            );

            instance.getDatabase().getDatastore().save(profile);

            instance.getLogger().info(CC.format("&bProfile for &6" + uuid + "&b created in Database."));
        }
        return profile;
    }

}
