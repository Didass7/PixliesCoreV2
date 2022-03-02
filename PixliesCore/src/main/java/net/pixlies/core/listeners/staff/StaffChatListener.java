package net.pixlies.core.listeners.staff;

import net.md_5.bungee.api.ChatColor;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.impl.StaffChatHandler;
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

    private static final Main instance = Main.getInstance();
    private final StaffChatHandler scHandler = instance.getHandlerManager().getHandler(StaffChatHandler.class);

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        String message = event.getMessage();

        event.setCancelled(true);

        if (user.getSettings().isInStaffChat()) {
            scHandler.sendStaffChat(Lang.STAFF + ChatColor.GOLD + player.getName() +
                    ChatColor.DARK_GRAY + " >> " + ChatColor.GRAY + message);
        }
    }

}
