package net.pixlies.core.listeners.staff;

import com.google.gson.JsonObject;
import net.pixlies.core.Main;
import net.pixlies.core.database.redis.RedisMessageReceiveEvent;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.StaffChatHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * StaffChat listener
 * @author vPrototype_
 * @author dynmie
 */
public class StaffChatListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final StaffChatHandler scHandler = instance.getHandlerManager().getHandler(StaffChatHandler.class);

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) { // legacy color/chat support
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        String message = event.getMessage();

        if (!user.isInStaffChat()) {
            return;
        }

        event.setCancelled(true);
        scHandler.sendStaffChat(player, message);
    }

    @EventHandler
    public void onChat(RedisMessageReceiveEvent event) {
        if (!event.getIdentifier().equals(StaffChatHandler.STAFF_CHAT_IDENTIFIER)) return;
        JsonObject jsonObject = event.getData();

        String senderName = jsonObject.get("senderName").getAsString();
        String serverName = jsonObject.get("serverName").getAsString();
        String message = jsonObject.get("message").getAsString();

        Lang.STAFF_CHAT_FORMAT.broadcastPermission("pixlies.staff.staffchat",
                "%PLAYER%;" + senderName,
                "%SERVER%;" + serverName,
                "%MESSAGE%;" + message
        );
    }

}
