package net.pixlies.core.listeners.player;

import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class CombatListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        user.setInCombat(false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHurtByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!Main.getInstance().getConfig().getBoolean("combat.timer", true)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        Player damager;
        if (event.getDamager() instanceof Projectile projectile) {
            if (!(projectile.getShooter() instanceof Player damagerA)) return;
            damager = damagerA;
        } else if (event.getDamager() instanceof Player damagerA) {
            damager = damagerA;
        } else {
            return;
        }

        if (victim.getUniqueId().equals(damager.getUniqueId())) return;

        User victimUser = User.get(victim.getUniqueId());
        User damagerUser = User.get(damager.getUniqueId());

        if (victimUser.isPassive() || victimUser.isBypassing()) return;
        if (damagerUser.isPassive() || damagerUser.isBypassing()) return;

        if (!victimUser.isInCombat()) {
            Lang.COMBAT_HIT.send(victim);
        }

        if (!damagerUser.isInCombat()) {
            Lang.COMBAT_HIT.send(damager);
        }

        victimUser.setInCombat(true);
        damagerUser.setInCombat(true);

    }

}
