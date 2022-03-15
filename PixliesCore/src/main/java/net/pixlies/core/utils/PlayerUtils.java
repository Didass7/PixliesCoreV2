package net.pixlies.core.utils;

import net.pixlies.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    public static @Nullable Player getRandomPlayer() {
        Collection<? extends Player> players = instance.getServer().getOnlinePlayers();
        if (players.isEmpty()) return null;
        return new ArrayList<Player>(players).get(new Random().nextInt(players.size()));
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
        player.setRemainingAir(player.getMaximumAir());
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    public static ItemStack getSkull(UUID uuid) {
        ItemStack skull = new ItemStack(Material.SKELETON_SKULL, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getPlayer(uuid));
        skull.setItemMeta(meta);
        return skull;
    }

}
