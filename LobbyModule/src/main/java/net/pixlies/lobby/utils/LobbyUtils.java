package net.pixlies.lobby.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.PlayerUtils;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.config.Config;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public final class LobbyUtils {

    private LobbyUtils() {}

    public static final String JOIN_TITLE = "§r§x§4§E§D§E§D§B§lP§r§x§4§D§C§9§C§7§li§r§x§4§1§B§0§A§E§lx§r§x§3§B§A§1§9§F§ll§r§x§3§4§8§C§8§B§li§r§x§3§0§7§A§7§A§le§r§x§2§9§6§9§6§9§ls§r§x§9§C§9§C§9§C§lN§r§x§A§8§A§8§A§8§le§r§x§A§D§A§D§A§D§lt§r§x§B§3§B§3§B§3§lw§r§x§B§D§B§D§B§D§lo§r§x§D§4§D§4§D§4§lr§r§f§lk§r§7!";
    public static final String JOIN_SUBTITLE = "§7Welcome!";

    private static final Lobby instance = Lobby.getInstance();
    private static final Config config = instance.getConfig();

    public static void resetPlayer(Player player) {
        User user = User.get(player.getUniqueId());

        if (user.getSettings().isInStaffMode()) {
            return;
        }

        PlayerUtils.heal(player);
        user.teleportToSpawn();
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, config.getInt("walkspeed.level", 1), false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, Integer.MAX_VALUE,false, false, false)); // 5 seconds
        player.sendTitle(JOIN_TITLE, JOIN_SUBTITLE);
        player.setBedSpawnLocation(null, true);
        player.setExp(0);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
        player.setFoodLevel(20);
        player.setInvulnerable(true);
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(player.hasPermission("pixlies.lobby.flight")); // if player has permission/cosmetic to fly allow
        player.getInventory().setHeldItemSlot(4); // Center of hotbar
        JoinItems.give(player);
    }

}
