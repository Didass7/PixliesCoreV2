package net.pixlies.core.entity.user;

import com.mongodb.client.model.Filters;
import lombok.*;
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
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {

    public static final Main instance = Main.getInstance();

    private final String uuid;
    private boolean loaded = false;
    private @Getter(AccessLevel.NONE) boolean joinedBefore = false;
    private long joined = System.currentTimeMillis();
    private @Nullable String discordId;
    private @Nullable String nickName;
    private List<Wallet> wallets = new ArrayList<>();
    private List<String> knownUsernames = new ArrayList<>();
    private List<UUID> blockedUsers = new ArrayList<>();

    // STATS
    private int civilPoints = 0; // Range: -100 to 100
    private int buyOrdersMade = 0;
    private int sellOrdersMade = 0;
    private int tradesMade = 0;
    private double moneySpent = 0d;
    private double moneyGained = 0d;
    private int itemsSold = 0;
    private int itemsBought = 0;

    // PUNISHMENT
    private Map<String, Punishment> currentPunishments = new HashMap<>();

    // PERSONALIZATION
    private boolean commandSpyEnabled = false;
    private boolean socialSpyEnabled = false;
    private boolean viewMutedChat = true;
    private boolean viewBannedJoins = true;
    private boolean joinVanish = false;
    private String scoreboardType = ScoreboardType.ENABLED.name();

    // SETTINGS
    private @Getter(AccessLevel.NONE) boolean inStaffMode = false;
    private @Getter(AccessLevel.NONE) boolean bypassing = false;
    private @Getter(AccessLevel.NONE) boolean vanished = false;
    private @Getter(AccessLevel.NONE) boolean passive = false;
    private boolean inStaffChat = false;

    // LANG
    private String lang = "ENG";

    // UNSAVED FIELDS
    private final transient Map<String, Timer> allTimers = new HashMap<>();

    public User(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public @NotNull OfflinePlayer getAsOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.getUniqueId());
    }

    public boolean hasJoinedBefore() {
        return joinedBefore;
    }

    public boolean isOnline() {
        return this.getAsOfflinePlayer().isOnline();
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
        UUID punisherUUID = punisher instanceof Player player
                ? player.getUniqueId()
                : UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

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
                duration + System.currentTimeMillis()
        );

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

            instance.getServer().getScheduler().runTask(instance, () -> player.kickPlayer(message));
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

            instance.getServer().getScheduler().runTask(instance, () -> player.kickPlayer(message));
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
            instance.getServer().getScheduler().runTask(instance, () -> player.kickPlayer(message));
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
            return ScoreboardType.ENABLED;
        }
    }


    // STATS
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

    public Document toDocument() {
        Document document = new Document();

        document.put("uuid", uuid);
        document.put("joined", joined);
        document.put("discordId", discordId);
        document.put("nickName", nickName);
        document.put("wallets", wallets);
        document.put("knownUsernames", knownUsernames);
        document.put("blockedUsers", blockedUsers);

        document.put("civilPoints", civilPoints);
        document.put("buyOrdersMade", buyOrdersMade);
        document.put("sellOrdersMade", sellOrdersMade);
        document.put("tradesMade", tradesMade);
        document.put("moneySpent", moneySpent);
        document.put("moneyGained", moneyGained);
        document.put("itemsSold", itemsSold);
        document.put("itemsBought", itemsBought);

        Document punishmentsDocument = new Document();
        for (Map.Entry<String, Punishment> entry : currentPunishments.entrySet()) {
            punishmentsDocument.put(entry.getKey(), entry.getValue().toDocument());
        }
        document.put("currentPunishments", punishmentsDocument);

        document.put("commandSpyEnabled", commandSpyEnabled);
        document.put("socialSpyEnabled", socialSpyEnabled);
        document.put("viewMutedChat", viewMutedChat);
        document.put("viewBannedJoins", viewBannedJoins);
        document.put("joinVanish", joinVanish);
        document.put("scoreboardType", scoreboardType);

        document.put("inStaffMode", inStaffMode);
        document.put("bypassing", bypassing);
        document.put("vanished", vanished);
        document.put("passive", passive);
        document.put("inStaffChat", inStaffChat);

        return document;
    }

    public void loadFromDocument(Document document) {
        joinedBefore = true;
        joined = document.getLong("joined") == null ? joined : document.getLong("joined");
        discordId = document.getString("discordId");
        nickName = document.getString("nickName");
        wallets = document.getList("wallets", Wallet.class) == null ? wallets: new ArrayList<>(document.getList("wallets", Wallet.class));
        knownUsernames = document.getList("knownUsernames", String.class) == null ? knownUsernames : new ArrayList<>(document.getList("knownUsernames", String.class));
        blockedUsers = document.getList("blockedUsers", UUID.class) == null ? blockedUsers : new ArrayList<>(document.getList("blockedUsers", UUID.class));

        civilPoints = document.getInteger("civilPoints") == null ? civilPoints : document.getInteger("civilPoints");
        buyOrdersMade = document.getInteger("buyOrdersMade") == null ? buyOrdersMade : document.getInteger("buyOrdersMade");
        sellOrdersMade = document.getInteger("sellOrdersMade") == null ? sellOrdersMade : document.getInteger("sellOrdersMade");
        tradesMade = document.getInteger("tradesMade") == null ? tradesMade: document.getInteger("tradesMade");
        moneySpent = document.getDouble("moneySpent") == null ? moneySpent : document.getDouble("moneySpent");
        moneyGained = document.getDouble("moneyGained") == null ? moneyGained : document.getDouble("moneyGained");
        itemsSold = document.getInteger("itemsSold") == null ? itemsSold : document.getInteger("itemsSold");
        itemsBought = document.getInteger("itemsBought") == null ? itemsBought : document.getInteger("itemsBought");

        Document punishmentsDocument = (Document) document.get("currentPunishments");
        if (punishmentsDocument != null) {
            currentPunishments.clear();
            for (String punishmentKey : punishmentsDocument.keySet()) {
                Document singlePunishmentDocument = (Document) punishmentsDocument.get(punishmentKey);
                Punishment punishment = new Punishment(singlePunishmentDocument);
                currentPunishments.put(punishmentKey, punishment);
            }
        }

        commandSpyEnabled = document.getBoolean("commandSpyEnabled") == null ? commandSpyEnabled : document.getBoolean("commandSpyEnabled");
        socialSpyEnabled = document.getBoolean("socialSpyEnabled") == null ? socialSpyEnabled : document.getBoolean("socialSpyEnabled");
        viewMutedChat = document.getBoolean("viewMutedChat") == null ? viewMutedChat : document.getBoolean("viewMutedChat");
        viewBannedJoins = document.getBoolean("viewBannedJoins") == null ? viewBannedJoins : document.getBoolean("viewBannedJoins");
        joinVanish = document.getBoolean("joinVanish") == null ? joinVanish : document.getBoolean("joinVanish");
        scoreboardType = document.getString("scoreboardType") == null ? scoreboardType : document.getString("scoreboardType");

        inStaffMode = document.getBoolean("inStaffMode") == null ? inStaffMode : document.getBoolean("inStaffMode");
        bypassing = document.getBoolean("bypassing") == null ? bypassing : document.getBoolean("bypassing");
        vanished = document.getBoolean("vanished") == null ? vanished : document.getBoolean("vanished");
        passive = document.getBoolean("passive") == null ? passive : document.getBoolean("passive");
        inStaffChat = document.getBoolean("inStaffChat") == null ? inStaffChat : document.getBoolean("inStaffChat");

    }

    /**
     * Loads the user from the database.
     * This should be run async to the main thread.
     * @param cache True to cache the user to the cache, false to not.
     * @return True if the user has saved data.
     */
    public boolean load(boolean cache) {
        Document document = instance.getMongoManager().getUsersCollection().find(Filters.eq("uuid", uuid)).first();
        if (document == null) {
            backup();
            loaded = true;
            if (cache) {
                instance.getMongoManager().getUserCache().put(getUniqueId(), this);
            }
            return false;
        }
        loadFromDocument(document);
        loaded = true;

        if (cache) {
            instance.getMongoManager().getUserCache().put(getUniqueId(), this);
        }
        return true;
    }

    public void save() {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, this::backup);
    }

    public void backup() {
        if (instance.getMongoManager().getUsersCollection().find(Filters.eq("uuid", uuid)).first() == null) {
            instance.getMongoManager().getUsersCollection().insertOne(toDocument());
        }
        instance.getMongoManager().getUsersCollection().findOneAndReplace(Filters.eq("uuid", uuid), toDocument());
    }

    /**
     * Removes the user from the user cache.
     */
    public void removeFromCache() {
        instance.getMongoManager().getUserCache().remove(getUniqueId());
    }

    public void cache() {
        instance.getMongoManager().getUserCache().put(getUniqueId(), this);
    }

    // STATICS - it's not static abuse if you use it properly.

    /**
     * Get a user from a UUID
     * @param uuid The player's UUID
     * @return A user from the cache
     * @see User
     */
    public static User get(UUID uuid) {
        if (!instance.getMongoManager().getUserCache().containsKey(uuid)) new User(uuid).cache();
        return instance.getMongoManager().getUserCache().get(uuid);
    }

    /**
     * A static method to get a User without caching it to the user cache.
     * This should be run async to the main thread.
     * @param uuid the UUID of the player.
     * @see User
     * @see UUID
     * @return A loaded uncached user.
     */
    public static User getLoadDoNotCache(UUID uuid) {
        if (instance.getMongoManager().getUserCache().containsKey(uuid)) {
            User user = instance.getMongoManager().getUserCache().get(uuid);
            if (!user.isLoaded()) {
                user.load(false);
            }
        }
        User user = new User(uuid);
        user.load(false);
        return user;
    }

    /**
     * Reload all user data for all online players
     */
    public static void loadAllOnlineUsers() {
        for (Player player : instance.getServer().getOnlinePlayers()) {
            User user = User.get(player.getUniqueId());
            if (!user.isLoaded() && user.isOnline()) {
                user.load(true);
            }
        }
    }

}
