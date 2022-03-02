package net.pixlies.core.listeners.staff;

import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Staffchat listener
 * @author vPrototype_
 */
public class StaffChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        String message = event.getMessage();

        event.setCancelled(true);

        if (user.getSettings().isInStaffChat()) {
            Lang.STAFFCHAT_FORMAT.broadcastPermission("pixlies.staff.staffchat", "%PLAYER%;" + player.getName(),
                    "%MESSAGE%;" + message);
        }
    }

}
