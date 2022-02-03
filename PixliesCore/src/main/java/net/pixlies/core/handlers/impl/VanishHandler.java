package net.pixlies.core.handlers.impl;

import net.pixlies.core.entity.User;
import net.pixlies.core.events.impl.moderation.VanishStatusChangeEvent;
import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.EventUtils;
import org.bukkit.entity.Player;

import java.util.*;

public class VanishHandler implements Handler {

    private static final Main instance = Main.getInstance();

    private final PassiveHandler handler = instance.getHandlerManager().getHandler(PassiveHandler.class);

    public boolean vanish(Player player) {
        val event = new VanishStatusChangeEvent(player, VanishStatusChangeEvent.VanishState.VANISH);
        EventUtils.callEvent(event);
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isVanished()) return false;
        for (Player target : instance.getServer().getOnlinePlayers()) {
            if (target.hasPermission("pixlies.moderation.vanish.exempt")) continue;
            target.hidePlayer(instance, player);
        }
        handler.setPassive(player, true);
        user.getSettings().setVanished(true);
        return true;
    }

    public boolean unvanish(Player player) {
        val event = new VanishStatusChangeEvent(player, VanishStatusChangeEvent.VanishState.UNVANISH);
        EventUtils.callEvent(event);
        if (event.isCancelled()) return false;
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isVanished()) return false;
        player.setAllowFlight(player.hasPermission("pixlies.fly"));
        for (Player target : instance.getServer().getOnlinePlayers()) {
            target.showPlayer(instance, player);
        }
        if (!user.getSettings().isStaffModeEnabled()) {
            handler.setPassive(player, false);
        }
        user.getSettings().setVanished(false);
        return true;
    }

    public void setVanished(Player player, boolean state) {
        if (state) {
            vanish(player);
        } else {
            unvanish(player);
        }
    }

    public Collection<Player> getVanishedPlayers() {
        List<Player> players = new ArrayList<>();
        instance.getDatabase().getUserCache().forEach((uuid, user) -> {
            Player player = instance.getServer().getPlayer(uuid);
            if (player == null) return;
            if (!user.getSettings().isVanished()) return;
            players.add(player);
        });
        return players;
    }

}
