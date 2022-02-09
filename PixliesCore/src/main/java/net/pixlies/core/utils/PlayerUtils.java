package net.pixlies.core.utils;

import net.pixlies.core.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PlayerUtils {

    private static final Main instance = Main.getInstance();

    private PlayerUtils() {}

    @Nullable
    public static Player getRandomPlayer(@NotNull Player player) {
        List<Player> players = new ArrayList<>(instance.getServer().getOnlinePlayers());

        players.remove(player);
        if (players.isEmpty()) return null; // second check so we dont get outofbounds

        Random random = new Random();

        return players.get(random.nextInt(players.size()));
    }

    public static void heal(@NotNull Player player) {
        AttributeInstance health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        player.setHealth(health == null ? 20 : health.getBaseValue());
        player.setFoodLevel(20);
        player.setFreezeTicks(0);
        player.setFireTicks(0);
        player.setVisualFire(false);
        player.setArrowsInBody(0);
        player.setArrowsStuck(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

}
