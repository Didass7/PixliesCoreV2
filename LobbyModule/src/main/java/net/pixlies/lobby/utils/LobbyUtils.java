package net.pixlies.lobby.utils;

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

    private static final Lobby instance = Lobby.getInstance();
    private static final Config config = instance.getConfig();

    public static void resetPlayer(Player player) {
        PlayerUtils.heal(player);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, config.getInt("walkspeed.level", 1), false, false, false));
        player.setBedSpawnLocation(null, true);
        player.setExp(0);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
        player.setFoodLevel(20);
        player.setInvulnerable(true);
        player.setFreezeTicks(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(instance.getLobbyManager().getSpawnLocation());
        player.setAllowFlight(player.hasPermission("pixlies.lobby.flight"));
        player.getInventory().setHeldItemSlot(4);
        JoinItems.give(player);
    }

}
