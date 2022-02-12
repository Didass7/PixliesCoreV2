package net.pixlies.core.entity;

import com.google.gson.Gson;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.Main;
import net.pixlies.core.economy.Wallet;
import net.pixlies.core.entity.data.PermissionProfile;
import net.pixlies.core.entity.data.UserPersonalization;
import net.pixlies.core.entity.data.UserSettings;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import net.pixlies.core.moderation.PunishmentType;
import net.pixlies.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.*;

@Data
@AllArgsConstructor
@Entity("users")
public class User {

    private static final Main instance = Main.getInstance();

    private static final Gson gson = new Gson();

    @Id
    private String uuid;
    private long joined;
    private String discordId;
    private String nationId;
    private String nationRank;
    private String nickName;
    private Map<String, Wallet> wallets;
    private List<String> knownUsernames;
    private List<UUID> blockedUsers;
    private Map<String, Object> stats;
    private Map<String, Punishment> currentPunishments;
    private UserPersonalization personalization;
    private UserSettings settings;
    private String lang;
    private PermissionProfile permissionProfile;

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

    public Punishment mute(String reason, CommandSender punisher, boolean silent) {
        UUID punisherUUID = !(punisher instanceof Player player) ? UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670") : player.getUniqueId();
        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.MUTE, punisherUUID, System.currentTimeMillis(), reason, 0);
        currentPunishments.put("mute", punishment);
        if (silent) {
            Lang.PLAYER_PERMANENTLY_MUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        } else {
            Lang.PLAYER_PERMANENTLY_MUTED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        }
        save();
        return punishment;
    }

    public void unmute(CommandSender theGuy, boolean silent) {
        tempMute("Unmuted", theGuy, 0, silent);
    }

    public Punishment tempMute(String reason, CommandSender punisher, long duration, boolean silent) {
        UUID punisherUUID = punisher.getName().equalsIgnoreCase("console") ? UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670") : ((Player)punisher).getUniqueId();
        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.MUTE, punisherUUID, System.currentTimeMillis(), reason, duration + System.currentTimeMillis());
        currentPunishments.put("mute", punishment);
        if (silent)
            Lang.PLAYER_TEMPORARILY_MUTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
        else
            Lang.PLAYER_TEMPORARILY_MUTED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason, "%TIME%;" + new PrettyTime().format(new Date(punishment.getUntil())));
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
        return getBan() == null;
    }

    public Punishment ban(String reason, CommandSender punisher, boolean silent) {
        UUID punisherUUID = punisher.getName().equalsIgnoreCase("console") ? UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670") : ((Player)punisher).getUniqueId();
        Punishment punishment = new Punishment(UUID.randomUUID().toString(), PunishmentType.BAN, punisherUUID, System.currentTimeMillis(), reason, 0);
        currentPunishments.put("ban", punishment);
        if (silent)
            Lang.PLAYER_PERMANENTLY_BANNED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        else
            Lang.PLAYER_PERMANENTLY_BANNED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        save();
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
        save();
        return punishment;
    }

    public void unban(CommandSender theGuy) {
        tempBan("Unbanned", theGuy, 0, true);
    }

    public Punishment getBlacklist() {
        if (!currentPunishments.containsKey("blacklist")) return null;
        return currentPunishments.get("blacklist");
    }

    public boolean isBlacklisted() {
        return currentPunishments.containsKey("blacklist");
    }

    public Punishment blacklist(String reason, CommandSender punisher, boolean silent) {
        UUID punisherUUID = punisher instanceof Player player ? player.getUniqueId() : UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");
        Punishment punishment = new Punishment(UUID.randomUUID().toString(),
                PunishmentType.BLACKLIST,
                punisherUUID,
                System.currentTimeMillis(),
                reason,
                0
        );
        currentPunishments.put("blacklist", punishment);
        if (silent) {
            Lang.PLAYER_BLACKLISTED.broadcastPermission("pixlies.moderation.silent", "%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        } else {
            Lang.PLAYER_BLACKLISTED.broadcast("%PLAYER%;" + this.getAsOfflinePlayer().getName(), "%EXECUTOR%;" + punisher.getName(), "%REASON%;" + reason);
        }
        save();
        return punishment;
    }

    public void unblacklist(CommandSender sender) {
        if (!isBlacklisted()) return;
        currentPunishments.remove("blacklist");
    }

    public boolean hasNickName() {
        if (nickName == null) return false;
        return !nickName.equals("NONE");
    }

    public void setNickName(String name) {
        if (name.length() > 16) {
            throw new IllegalArgumentException("Nickname length cannot be above 16. Current length: " + name.length());
        }
        nickName = name;
        save();
    }

    public String getNickName() {
        if (nickName == null) return getAsOfflinePlayer().getName();
        if (!nickName.equalsIgnoreCase("NONE")) {
            return nickName;
        }
        return getAsOfflinePlayer().getName();
    }

    /*

    public boolean inNation() {
        return !nationId.equalsIgnoreCase("NONE");
    }

    public void addToNation(String nationId, NationRank rank) {
        if (!inNation()) {
            this.nationId = nationId;
            this.nationRank = rank.getName();
            Nation nation = Nation.getFromId(nationId);
            nation.getMemberUUIDs().add(uuid);
            nation.save();
            save();
        }
    }

     */

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    public static User get(UUID uuid) {
        return instance.getDatabase().getUserCache().getOrDefault(uuid, getFromDatabase(uuid));
    }

    public static User getFromDatabase(UUID uuid) {
        User profile = instance.getDatabase().getDatastore().createQuery(User.class).field("uniqueId").contains(uuid.toString()).find().tryNext();
        if (profile == null) {
            profile = new User(
                    uuid.toString(),
                    System.currentTimeMillis(),
                    "NONE",
                    "NONE",
                    "NONE",
                    "NONE",
                    Wallet.getDefaultWallets(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new HashMap<>(),
                    new HashMap<>(),
                    UserPersonalization.getDefaults(),
                    UserSettings.getDefaults(),
                    "ENG",
                    new PermissionProfile(new ArrayList<>(), new ArrayList<>())
            );

            instance.getDatabase().getDatastore().save(profile);

            instance.getLogger().info(CC.format("&bProfile for &6" + uuid + "&b created in Database."));
        }
        return profile;
    }

    public void backup() {
        instance.getDatabase().getDatastore().save(this);
    }

    public void save() {
        instance.getDatabase().getUserCache().put(getUniqueId(), this);
    }
    
}
