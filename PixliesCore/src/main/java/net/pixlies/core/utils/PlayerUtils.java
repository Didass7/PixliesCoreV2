package net.pixlies.core.utils;

import lombok.val;
import net.pixlies.core.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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

}
