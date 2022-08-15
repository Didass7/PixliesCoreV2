package net.pixlies.core.handlers.impl;

import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.events.impl.moderation.VanishStatusChangeEvent;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.EventUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VanishHandler implements Handler {

    private static final Main instance = Main.getInstance();

    private final PassiveHandler handler = instance.getHandlerManager().getHandler(PassiveHandler.class);

    public boolean vanish(Player player, boolean withEvent) {

        // EVENT
        if (withEvent) {
            val event = new VanishStatusChangeEvent(player, VanishStatusChangeEvent.VanishState.VANISH);
            EventUtils.call(event);
            if (event.isCancelled()) return false;
        }

        User user = User.get(player.getUniqueId());

        // IF VANISHED RETURN
        if (user.isVanished()) return false;

        // LOOP PLAYERS
        for (Player target : instance.getServer().getOnlinePlayers()) {
            if (target.hasPermission("pixlies.moderation.vanish.exempt")) continue;
            if (target.getUniqueId().equals(player.getUniqueId())) continue;
            target.hidePlayer(instance, player);
        }

        // SET PASSIVE
        if (!user.isPassive()) {
            handler.setPassive(player, true);
        }

        // SET VANISHED
        user.setVanished(true);
        user.save();

        return true;
    }

    public boolean unvanish(Player player, boolean withEvent) {

        // EVENT
        if (withEvent) {
            val event = new VanishStatusChangeEvent(player, VanishStatusChangeEvent.VanishState.UNVANISH);
            EventUtils.call(event);
            if (event.isCancelled()) return false;
        }

        User user = User.get(player.getUniqueId());

        // IF IS UNVANISHED RETURN
        if (!user.isVanished()) return false;

        player.setAllowFlight(player.hasPermission("pixlies.fly"));
        for (Player target : instance.getServer().getOnlinePlayers()) {
            if (target.getUniqueId().equals(player.getUniqueId())) continue;
            target.showPlayer(instance, player);
        }

        // IF STAFF MODE IS NOT ENABLED, DISABLE PASSIVE
        if (!user.isInStaffMode()) {
            handler.setPassive(player, false);
        }

        // SET VANISHED FALSE
        user.setVanished(false);
        user.save();

        return true;
    }

    /**
     * Change vanish states with the event called.
     * @see VanishStatusChangeEvent
     * @param player The player to vanish
     * @param state True for on, False for off
     */
    public void setVanished(Player player, boolean state) {
        if (state) {
            vanish(player, true);
        } else {
            unvanish(player, true);
        }
    }

    public Collection<Player> getVanishedPlayers() {
        List<Player> players = new ArrayList<>();
        instance.getDatabase().getUserCache().forEach((uuid, user) -> {
            Player player = instance.getServer().getPlayer(uuid);
            if (player == null) return;
            if (!user.isVanished()) return;
            players.add(player);
        });
        return players;
    }

}
