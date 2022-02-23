/**
 * @author vPrototype_
 */

package net.pixlies.core.listeners.cosmetics;

import net.pixlies.core.Main;
import net.pixlies.core.entity.Poll;
import net.pixlies.core.handlers.impl.PollHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PollListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final PollHandler pollHandler = instance.getHandlerManager().getHandler(PollHandler.class);

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (pollHandler.getPollsInCreation().containsKey(player.getUniqueId())) {
            Poll poll = pollHandler.getPollsInCreation().get(player.getUniqueId());

            event.setCancelled(true);

            if (message.equalsIgnoreCase("cancel")) {
                Lang.POLL_CANCELLED.send(player);
                pollHandler.getPollsInCreation().remove(player.getUniqueId());
                return;
            }

            int size = poll.getPollInfo().size();

            // Check if they want to end the poll creation process
            if (size >= 3 && message.equalsIgnoreCase("finish")) {
                poll.makePublic();
                pollHandler.getPollsInCreation().remove(player.getUniqueId());
                pollHandler.getPolls().put(poll.getId(), poll);
                return;
            }

            Lang.ADDED_POLL_OPTION.send(player, "%NUMBER%;" + size, "%OPTION%;" + message);
            Lang.ENTER_POLL_OPTION.send(player, "%NUMBER%;" + (size + 1));

            // Two options is the minimum
            if (size == 2) {
                Lang.HOW_TO_END_POLL_CREATION.send(player);
            }

            poll.addAnswer(message);
            player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 100, 1);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        pollHandler.getPollsInCreation().remove(event.getPlayer().getUniqueId());
    }

}
