package net.pixlies.core.listeners.moderation;

import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpyListener implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        String cmd = event.getMessage();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            User user = User.get(p.getUniqueId());
            if (user.getPersonalization().isCommandSpyEnabled() && p.hasPermission("pixlies.moderation.commandspy")) {
                p.sendMessage(Lang.COMMAND + "§3" + player.getName() + " §8- §7" + cmd);
            }
        }
    }

}
