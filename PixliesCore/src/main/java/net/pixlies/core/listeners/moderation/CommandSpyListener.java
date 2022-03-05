package net.pixlies.core.listeners.moderation;

import net.pixlies.core.entity.user.User;
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

        // Staff setting: commandspy
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            User user = User.get(p.getUniqueId());
            if (user.getPersonalization().isCommandSpyEnabled()) {
                Lang.MODERATION_COMMANDSPY_FORMAT.send(p, "%PLAYER%;" + player.getName(), "%MESSAGE%;" + cmd);
            }
        }
    }

}
