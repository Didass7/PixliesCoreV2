package net.pixlies.core.entity.user;

import com.google.gson.Gson;
import com.mongodb.lang.Nullable;
import dev.morphia.annotations.*;
import dev.morphia.query.experimental.filters.Filters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.economy.Wallet;
import net.pixlies.core.entity.Warp;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.entity.user.timers.impl.CombatTimer;
import net.pixlies.core.entity.user.timers.impl.TeleportTimer;
import net.pixlies.core.house.House;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import net.pixlies.core.moderation.PunishmentType;
import net.pixlies.core.scoreboard.ScoreboardType;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.*;

@Data
@AllArgsConstructor
@Entity("users")
@Indexes(
        @Index(fields = { @Field("uuid") })
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

    // STATS
    private String dateJoined; // In Pixlies DateAndTime
    private Map<House, Integer> houses; // House & House XPs
    private int civilPoints; // Range: -100 to 100
    private int buyOrdersMade;
    private int sellOrdersMade;
    private int tradesMade;
    private double moneySpent;
    private double moneyGained;
    private int itemsSold;
    private int itemsBought;

    // PUNISHMENT
    private Map<String, Punishment> currentPunishments;

    // PERSONALIZATION
    private boolean commandSpyEnabled;
    private boolean socialSpyEnabled;
    private boolean viewMutedChat;
    private boolean viewBannedJoins;
    private boolean joinVanish;
    private String scoreboardType;

    // SETTINGS
    private @Getter(AccessLevel.NONE) boolean inStaffMode;
    private @Getter(AccessLevel.NONE) boolean bypassing;
    private @Getter(AccessLevel.NONE) boolean vanished;
    private @Getter(AccessLevel.NONE) boolean passive;
    private boolean inStaffChat;

    // LANG
    private String lang;
    @Getter(AccessLevel.NONE) private Map<String, Object> extras;
    private final transient Map<String, Timer> allTimers = new HashMap<>();

    public Map<String, Object> getExtras() {
        if (extras == null) extras = new HashMap<>();
        return extras;
    }

    public Wallet getServerCurrency() {
        return wallets.get("serverCurrency");
    }

    public OfflinePlayer getAsOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.getUniqueId());
    }

    public boolean isOnline() {
        OfflinePlayer player = getAsOfflinePlayer();
        if (player == null) {
            return false;
        }
        return player.isOnline();
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
                punisher.getName(),
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

        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.MUTE.name(), punisherUUID.toString(), punisher.getName(), System.currentTimeMillis(), newReason, duration + System.currentTimeMillis());
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

        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.BAN.name(), punisherUUID.toString(), punisher.getName(), System.currentTimeMillis(), newReason, 0);
        currentPunishments.put("ban", punishment);
        if (silent)
            Lang.PLAYER_PERMANENTLY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        else
            Lang.PLAYER_PERMANENTLY_BANNED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + newReason);
        if (getAsOfflinePlayer().getPlayer() != null && getAsOfflinePlayer().isOnline()) {
            Player player = getAsOfflinePlayer().getPlayer();
            String message = Lang.BAN_MESSAGE.get(player)
                    .replace("%REASON%", punishment.getReason())
                    .replace("%EXECUTOR%", punisher.getName())
                    .replace("%ID%", punishment.getPunishmentId())
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

        Punishment punishment = new Punishment(
                UUID.randomUUID().toString(),
                PunishmentType.BAN.name(),
                punisherUUID.toString(),
                punisher.getName(),
                System.currentTimeMillis(),
                newReason,
                duration + System.currentTimeMillis()
        );

        currentPunishments.put("ban", punishment);
        if (silent)
            Lang.PLAYER_TEMPORARILY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        else
            Lang.PLAYER_TEMPORARILY_BANNED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + newReason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));

        if (getAsOfflinePlayer().getPlayer() != null && getAsOfflinePlayer().isOnline()) {
            Player player = getAsOfflinePlayer().getPlayer();
            String message = Lang.BAN_MESSAGE.get(player)
                    .replace("%REASON%", punishment.getReason())
                    .replace("%EXECUTOR%", punisher.getName())
                    .replace("%ID%", punishment.getPunishmentId())
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
                punisher.getName(),
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
                    .replace("%EXECUTOR%", punisher.getName())
                    .replace("%ID%", punishment.getPunishmentId());
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
                .replace("%EXECUTOR%", sender.getName())
                .replace("%REASON%",  kickReason);
        player.kick(Component.text(kickMessage));
        if (silent) {
            Lang.PLAYER_KICKED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName(), "%REASON%;" + kickReason);
        } else {
            Lang.PLAYER_KICKED.broadcast("%PLAYER%;" + getAsOfflinePlayer().getName(), "%EXECUTOR%;" + sender.getName(), "%REASON%;" + kickReason);
        }
        return true;
    }

//    public @NotNull Punishment marketRestrict(Player punisher, String reason) {
//        Punishment punishment = new Punishment(UUID.randomUUID().toString(),
//                PunishmentType.MARKET_RESTRICT.name(),
//                punisher.getUniqueId().toString(),
//                System.currentTimeMillis(),
//                reason,
//                0
//        );
//        getCurrentPunishments().put("marketRestrict", punishment);
//        save();
//        return punishment;
//    }
//
//    public void unRestrict() {
//        getCurrentPunishments().remove("marketRestrict");
//        save();
//    }

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
        if (name.equals(getAsOfflinePlayer().getName())) {
            nickName = null;
            save();
            return;
        }
        if (name.length() > 16 || name.isEmpty()) {
            throw new IllegalArgumentException("Illegal nickname. Current length: " + name.length());
        }
        nickName = name;
        save();
    }

    public void removeNickName() {
        nickName = null;
        save();
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

    public boolean isInStaffMode() {
        Player player = getAsOfflinePlayer().getPlayer();
        if (player == null) return false;
        return player.hasPermission("pixlies.moderation.staffmode") && inStaffMode;
    }

    public boolean isBypassing() {
        Player player = getAsOfflinePlayer().getPlayer();
        if (player == null) return false;
        return player.hasPermission("pixlies.moderation.bypass") && bypassing;
    }

    public boolean isPassive() {
        Player player = getAsOfflinePlayer().getPlayer();
        if (player == null) return false;
        return (player.hasPermission("pixlies.moderation.vanish") && passive) || (player.hasPermission("pixlies.moderation.staffmode") && passive);
    }

    public boolean isVanished() {
        Player player = getAsOfflinePlayer().getPlayer();
        if (player == null) return false;
        return player.hasPermission("pixlies.moderation.vanish") && vanished;
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
        backup();
    }

    /**
     * Updates the timers list.
     * Use this to get timers.
     * @return All non-expired timers
     */
    public Collection<Timer> getTimers() {
        if (allTimers.isEmpty()) return Collections.emptyList();
        List<Timer> timers = new ArrayList<>(allTimers.values());
        timers.sort(Comparator.comparing(Timer::getDisplayName));
        return timers;
    }

    /**
     * Teleports the user to a location with a timer if the player is online.
     * Teleports instantly if the user is passive.
     * @param location The location to teleport to
     * @param timed False if it should instantly teleport.
     */
    public void teleport(@NotNull Location location, boolean timed) {
        if (!this.getAsOfflinePlayer().isOnline()) return;
        Player player = this.getAsOfflinePlayer().getPlayer();
        if (player == null) return;

        if (isInCombat()) return;

        if (timed && !bypassing && !passive && Main.getInstance().getConfig().getBoolean("warpSettings.timedTeleports", true)) {
            TeleportTimer timer = new TeleportTimer(System.currentTimeMillis());
            allTimers.put(TeleportTimer.ID, timer);

            new BukkitRunnable() {
                @Override
                public void run() {


                    if (allTimers.get(TeleportTimer.ID) == null) {
                        cancel();
                    }

                    if (isInCombat()) {
                        allTimers.remove(TeleportTimer.ID);
                        cancel();
                        return;
                    }

                    if (timer.isExpired()) {
                        allTimers.remove(TeleportTimer.ID);
                        player.teleport(location);
                        cancel();
                    }

                }
            }.runTaskTimer(Main.getInstance(), 1, 1);

            return;
        }

        player.teleport(location);
    }

    /**
     * Cancel an active teleportation timer.
     */
    public void cancelTeleport() {
        allTimers.remove(TeleportTimer.ID);
    }

    /**
     * Check if the user is currently teleporting with a timer.
     * @return True if the user is teleporting, false if the user is not.
     */
    public boolean isTeleporting() {
        return allTimers.containsKey(TeleportTimer.ID);
    }

    /**
     * @see User#teleport(Location, boolean)
     * @param location The location to teleport to
     */
    public void teleport(@NotNull Location location) {
        this.teleport(location, true);
    }

    /**
     * @see User#teleport(Location, boolean)
     * @param user The user to teleport to
     */
    public void teleport(@NotNull User user) {
        OfflinePlayer player = user.getAsOfflinePlayer();
        Location location = player.getLocation();
        if (location == null) {
            return;
        }
        this.teleport(location, true);
    }

    /**
     * Teleports the user to the spawn
     * @param timed False if it should instantly teleport.
     */
    public void teleportToSpawn(boolean timed) {
        Warp warp = Warp.getSpawn();
        Location location = warp.getAsBukkitLocation();
        this.teleport(location, timed);
    }

    /**
     * @see User#teleportToSpawn(boolean)
     */
    public void teleportToSpawn() {
        Warp warp = Warp.getSpawn();
        Location location = warp.getAsBukkitLocation();
        this.teleport(location, true);
    }

    /**
     * @see User#teleport(Location, boolean)
     * @param warp The warp to teleport to
     */
    public void teleport(@NotNull Warp warp) {
        Location location = warp.getAsBukkitLocation();
        this.teleport(location, true);
    }


    /**
     * Check if the user has any timers.
     * @return True if it has timers; False if no timers.
     */
    public boolean hasTimers() {
        return getTimers().size() > 0;
    }

    /**
     * Check if the user has a specific timer.
     * @param identifier The timer identifier
     * @return True if the timer exists; False if it doesn't.
     */
    public boolean hasTimer(String identifier) {
        return allTimers.containsKey(identifier);
    }

    /**
     * Set the scoreboard scoreboardType for the user.
     * @param scoreboardType The scoreboard scoreboardType
     * @see ScoreboardType
     */
    public void setScoreboardType(@NotNull ScoreboardType scoreboardType) {
        this.scoreboardType = scoreboardType.name();
    }

    /**
     * Set the scoreboard type for the user.
     * @return The scoreboard type
     * @see ScoreboardType
     */
    public @NotNull ScoreboardType getScoreboardType() {
        try {
            return ScoreboardType.valueOf(scoreboardType);
        } catch (IllegalArgumentException e) {
            return ScoreboardType.STANDARD;
        }
    }

    // STATS

    public House getHouse() {
        int max = Collections.max(houses.values());

        if (max < 200) return House.NOT_DECIDED;

        for (Map.Entry<House, Integer> entry : houses.entrySet()) {
            if (entry.getValue()==max) {
                return entry.getKey();
            }
        }

        return House.NOT_DECIDED;
    }

    public void addBuy() {
        buyOrdersMade += 1;
    }

    public void addSell() {
        sellOrdersMade += 1;
    }

    public void addTrade() {
        tradesMade += 1;
    }

    public void addMoneySpent(double money) {
        moneySpent += money;
    }

    public void addMoneyGained(double money) {
        moneyGained += money;
    }

    public void addItemsSold(int items) {
        itemsSold += items;
    }

    public void addItemsBought(int items) {
        itemsBought += items;
    }

    public boolean isInCombat() {
        if (isBypassing()) return false;
        if (isPassive()) return false;
        return allTimers.containsKey(CombatTimer.ID) && instance.getConfig().getBoolean("combat.timer", true) ;
    }

    public void setInCombat(boolean value) {
        if (!value) allTimers.remove(CombatTimer.ID);
        if (isBypassing() || isPassive()) allTimers.remove(CombatTimer.ID);

        if (allTimers.containsKey(CombatTimer.ID)) {
            Timer timer = allTimers.get(CombatTimer.ID);
            if (timer != null) {
                timer.setStartTime(System.currentTimeMillis());
                return;
            }
        }

        CombatTimer timer = new CombatTimer(System.currentTimeMillis());
        allTimers.put(CombatTimer.ID, timer);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!allTimers.containsKey(CombatTimer.ID)) {
                    cancel();
                    return;
                }
                if (timer.isExpired() || isBypassing() || isPassive()) {
                    allTimers.remove(CombatTimer.ID);
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 1, 1);
    }

    public String getPrefix() {
        return PlaceholderAPI.setPlaceholders(getAsOfflinePlayer(), "%luckperms_prefix%");
    }

    public String getSuffix() {
        return PlaceholderAPI.setPlaceholders(getAsOfflinePlayer(), "%luckperms_suffix%");
    }

    // STATICS - it's not static abuse if you use it properly.

    /**
     * Get a user from a UUID
     * @param uuid The player's UUID
     * @return A not null User
     * @see User
     */
    public static @NotNull User get(UUID uuid) {
        if (!instance.getDatabase().getUserCache().containsKey(uuid)) return getFromDatabase(uuid);
        return instance.getDatabase().getUserCache().get(uuid);
    }

    public static @NotNull Collection<User> getOnlineUsers() {
        List<User> users = new ArrayList<>();
        for (User user : instance.getDatabase().getUserCache().values()) {
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
                    null,
                    null,
                    Wallet.getDefaults(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    Main.getInstance().getCalendar().formatDateAndTime(),
                    new HashMap<>() {{
                        for (House house : House.values())
                            put(house, 0);
                    }},
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    new HashMap<>(),
                    false,
                    false,
                    false,
                    false,
                    false,
                    ScoreboardType.STANDARD.name(),
                    false,
                    false,
                    false,
                    false,
                    false,
                    "ENG",
                    new HashMap<>()
            );

            profile.save();
            instance.getLogger().info(CC.format("&bProfile for &6" + uuid + "&b created in database."));
            return profile;

        }

        // Null check in case there's an older User version
        User user = new User(
                profile.uuid, // id, can't be null in the first place
                profile.joined,
                profile.discordId, // nullable
                profile.nickName, // nullable
                profile.wallets == null ? Wallet.getDefaults() : profile.wallets,
                profile.knownUsernames == null ? new ArrayList<>() : profile.knownUsernames,
                profile.blockedUsers == null ? new ArrayList<>() : profile.blockedUsers,
                profile.dateJoined == null ? Main.getInstance().getCalendar().formatDate() : profile.dateJoined,
                profile.houses == null ? new HashMap<>() {{
                    for (House house : House.values()) put(house, 0);
                }} : profile.houses,
                profile.civilPoints,
                profile.buyOrdersMade,
                profile.sellOrdersMade,
                profile.tradesMade,
                profile.moneySpent,
                profile.moneyGained,
                profile.itemsSold,
                profile.itemsBought,
                profile.currentPunishments == null ? new HashMap<>() : profile.currentPunishments,
                profile.commandSpyEnabled,
                profile.socialSpyEnabled,
                profile.viewMutedChat,
                profile.viewBannedJoins,
                profile.joinVanish,
                profile.scoreboardType == null ? ScoreboardType.STANDARD.name() : profile.scoreboardType,
                profile.inStaffMode,
                profile.bypassing,
                profile.vanished,
                profile.passive,
                profile.inStaffChat,
                profile.lang == null ? "ENG" : profile.lang,
                profile.extras == null ? new HashMap<>() : profile.extras
        );

        user.save();
        return user;
    }

}
