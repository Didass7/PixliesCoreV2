package net.pixlies.core.utils;

import lombok.val;
import net.pixlies.core.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public final class PlayerUtils {

    private static final Main instance = Main.getInstance();

    private PlayerUtils() {}

    public static Player getRandomPlayer(Player player) {
        val onlinePlayers = new ArrayList<Player>(instance.getServer().getOnlinePlayers());
        if (onlinePlayers.isEmpty()) return null;
        onlinePlayers.remove(player);
        Random random = new Random(onlinePlayers.size());
        return onlinePlayers.get(random.nextInt());
    }

    public static void heal(Player player) {
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
        player.setFoodLevel(20);
        player.setFreezeTicks(0);
        player.setFireTicks(0);
        player.setVisualFire(false);
        player.setArrowsInBody(0);
        player.setArrowsStuck(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

}
