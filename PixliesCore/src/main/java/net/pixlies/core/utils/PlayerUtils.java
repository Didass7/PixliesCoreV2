package net.pixlies.core.utils;

import lombok.val;
import net.pixlies.core.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class PlayerUtils {

    private static final Main instance = Main.getInstance();

    private PlayerUtils() {}

    public static Player getRandomPlayer(Player player) {
        List<Player> players = new ArrayList<>(instance.getServer().getOnlinePlayers());
        instance.getServer().getOnlinePlayers().forEach(target -> {
            if (target.getUniqueId() == player.getUniqueId()) return;
            players.add(target);
        });
        Random random = new Random();
        return players.get(random.nextInt(players.size()));
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
