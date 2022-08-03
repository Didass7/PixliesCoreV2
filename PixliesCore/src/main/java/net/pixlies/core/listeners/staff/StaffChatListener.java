package net.pixlies.core.listeners.staff;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.StaffChatHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Staffchat listener
 * @author vPrototype_
 */
public class StaffChatListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final StaffChatHandler scHandler = instance.getHandlerManager().getHandler(StaffChatHandler.class);

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        String message = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());

        if (!user.isInStaffChat()) {
            return;
        }

        event.setCancelled(true);

        scHandler.sendStaffChat(player, message);
    }

}
