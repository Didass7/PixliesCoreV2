package net.pixlies.core.entity.user;

import com.mongodb.client.model.Filters;
import lombok.*;
import net.pixlies.core.Main;
import net.pixlies.core.economy.Wallet;
import net.pixlies.core.entity.user.timers.Timer;
import net.pixlies.core.scoreboard.ScoreboardType;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {

    private static final Main instance = Main.getInstance();

    private final String uuid;
    private boolean loaded = false;
    private boolean basicLoaded = false;
    private @Getter(AccessLevel.NONE) boolean joinedBefore = false;
    private long joined = System.currentTimeMillis();
    private @Nullable String discordId;
    private @Nullable String nickName;
    private List<Wallet> wallets = new ArrayList<>();
    private String currentUsername = "";
    private List<String> knownUsernames = new ArrayList<>();
    private String currentIp = "";
    private List<String> knownIps = new ArrayList<>();
    private List<UUID> blockedUsers = new ArrayList<>();

    // STATS
    private int civilPoints = 0; // Range: -100 to 100

    // PERSONALIZATION
    private boolean commandSpyEnabled = false;
    private boolean socialSpyEnabled = false;
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

    // CONSTRUCTORS
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

    public boolean hasNickName() {
        if (!getAsOfflinePlayer().isOnline()) return false;
        Player player = getAsOfflinePlayer().getPlayer();
        if (player != null) {
            if (!player.hasPermission("pixlies.player.nickname")) {
                return false;
            }
        }
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
    }

    public void removeNickName() {
        nickName = null;
    }

    public String getNickName() {

        if (!hasNickName()) return getAsOfflinePlayer().getName();
        return "*" + nickName;
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
     * Use this to get timers.
     * @return All timers
     */
    public Collection<Timer> getTimers() {
        if (allTimers.isEmpty()) return Collections.emptyList();
        List<Timer> timers = new ArrayList<>(allTimers.values());
        timers.sort(Comparator.comparing(Timer::getDisplayName));
        return timers;
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

    public void loadBasic() {
        Document document = instance.getMongoManager().getUsersCollection().find(Filters.eq("uuid", uuid)).first();
        if (document == null) {
            backup();
            basicLoaded = true;
            return;
        }
        loadBasicFromDocument(document);
        basicLoaded = true;
    }

    public void loadBasicFromDocument(Document document) {
        currentUsername = document.get("currentUsername", currentUsername);
        currentIp = document.get("currentIp", currentIp);
    }

    public Document toBasicDocument() {
        Document document = new Document();

        document.put("currentUsername", currentUsername);
        document.put("currentIp", currentIp);

        return document;
    }

    public Document toDocument() {
        Document document = new Document();

        document.put("uuid", uuid);
        document.put("joined", joined);
        document.put("discordId", discordId);
        document.put("nickName", nickName);
        document.put("wallets", wallets);
        document.put("currentUsername", currentUsername);
        document.put("knownUsernames", knownUsernames);
        document.put("currentIp", currentIp);
        document.put("knownIps", knownIps);
        document.put("blockedUsers", blockedUsers);

        document.put("civilPoints", civilPoints);

        document.put("commandSpyEnabled", commandSpyEnabled);
        document.put("socialSpyEnabled", socialSpyEnabled);
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
        this.loadBasic();

        joinedBefore = true;
        joined = document.getLong("joined") == null ? joined : document.getLong("joined");
        discordId = document.getString("discordId");
        nickName = document.getString("nickName");
        wallets = document.getList("wallets", Wallet.class) == null ? wallets: new ArrayList<>(document.getList("wallets", Wallet.class));
        currentUsername = document.get("currentUsername", currentUsername);
        knownUsernames = document.getList("knownUsernames", String.class) == null ? knownUsernames : new ArrayList<>(document.getList("knownUsernames", String.class));
        currentIp = document.get("currentIp", currentIp);
        knownIps = document.get("knownIps", knownIps);
        blockedUsers = document.getList("blockedUsers", UUID.class) == null ? blockedUsers : new ArrayList<>(document.getList("blockedUsers", UUID.class));

        //
        civilPoints = document.getInteger("civilPoints") == null ? civilPoints : document.getInteger("civilPoints");

        //
        commandSpyEnabled = document.getBoolean("commandSpyEnabled") == null ? commandSpyEnabled : document.getBoolean("commandSpyEnabled");
        socialSpyEnabled = document.getBoolean("socialSpyEnabled") == null ? socialSpyEnabled : document.getBoolean("socialSpyEnabled");
        joinVanish = document.getBoolean("joinVanish") == null ? joinVanish : document.getBoolean("joinVanish");
        scoreboardType = document.getString("scoreboardType") == null ? scoreboardType : document.getString("scoreboardType");

        //
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

    public boolean exists() {
        return instance.getMongoManager().getUsersCollection().find(Filters.eq("uuid", uuid)).first() != null;
    }

    public void backup() {
        if (instance.getMongoManager().getUsersCollection().find(Filters.eq("uuid", uuid)).first() == null) {
            instance.getMongoManager().getUsersCollection().insertOne(toDocument());
        }
        instance.getMongoManager().getUsersCollection().findOneAndReplace(Filters.eq("uuid", uuid), toDocument());
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
     * Gets a user from the cache if the user is saved in memory, else will create a new user object.
     * No data is guaranteed to be loaded.
     * @param uuid The UUID of the user
     * @return The User
     */
    public static User getActiveUser(UUID uuid) {
        if (instance.getMongoManager().getUserCache().containsKey(uuid)) {
            return instance.getMongoManager().getUserCache().get(uuid);
        }
        return new User(uuid);
    }

    /**
     * Reload all user data for all online players
     */
    public static void loadOnline() {
        for (Player player : instance.getServer().getOnlinePlayers()) {
            User user = User.get(player.getUniqueId());

            if (user.isOnline()) {
                user.load(true);
            }

            user.currentUsername = player.getName();

            InetSocketAddress address = player.getAddress();
            if (address != null) {
                user.currentIp = address.getAddress().getHostAddress();
            }
        }
    }

}
