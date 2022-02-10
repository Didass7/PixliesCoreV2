/**
 * @author vPrototype_
 */

package net.pixlies.core.listeners.cosmetics;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pixlies.core.Main;
import net.pixlies.core.entity.Poll;
import net.pixlies.core.handlers.impl.PollHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PollListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final PollHandler pollHandler = instance.getHandlerManager().getHandler(PollHandler.class);

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = String.valueOf(event.message());

        if (pollHandler.getPollsInCreation().containsKey(player.getUniqueId())) {
            Poll poll = pollHandler.getPollsInCreation().get(player.getUniqueId());

            event.setCancelled(true);

            if (message.equalsIgnoreCase("cancel")) {
                Lang.POLL_CANCELLED.send(player);
                poll = null;
                return;
            }

            int size = poll.getPollInfo().size();

            // Check if they want to end the poll creation process
            if (size >= 3 && message.equalsIgnoreCase("end")) {
                poll.makePublic();
                pollHandler.getPollsInCreation().remove(player.getUniqueId());
                pollHandler.getPolls().put(poll.getId(), poll);
                return;
            }

            Lang.ADDED_POLL_OPTION.send(player, "%NUMBER%;" + size, "%OPTION%;" + message);
            Lang.ENTER_POLL_OPTION.send(player, "%NUMBER%;" + size + 1);

            // Two options is the minimum
            if (size == 2) {
                Lang.HOW_TO_END_POLL_CREATION.send(player);
            }

            poll.addAnswer(message);
            player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 100, 1);
        }




        /*
                    SMPQOL.pollCreator.playSound(SMPQOL.pollCreator.getLocation(), "minecraft:entity.experience_orb.pickup", 100, 1);

        if (pollStage == 4 && message.equalsIgnoreCase("end")) {
            pollStage = 0;
            SMPQOL.constructingPoll = false;
            SMPQOL.activePoll = true;
            for (int i = 1; i < SMPQOL.pollInfo.size(); i++) {
                SMPQOL.pollVotes.put(SMPQOL.pollInfo.get(i), 0);
            }
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "  " + ChatColor.BOLD + "POLL STARTED! " + ChatColor.RESET + "" +
                    ChatColor.AQUA + "Use " + ChatColor.GREEN + "/poll view" + ChatColor.AQUA + " to view the info and vote!");
            Bukkit.broadcastMessage(" ");
            return;
        }
        for (String s : SMPQOL.pollInfo) {
            if (message.equalsIgnoreCase(s)) {
                player.sendMessage(ChatColor.RED + "Please try again by specifying something a little bit different than last time.");
                return;
            }
        }
        SMPQOL.pollInfo.add(message);
        player.sendMessage(ChatColor.YELLOW + "--------------------------------------------------------------");
        switch (pollStage) {
            case 1:
                player.sendMessage(ChatColor.GREEN + "Registered question: " + ChatColor.AQUA + message);
                player.sendMessage(ChatColor.GREEN + "Now, please enter the first option/choice of your poll.");
                player.sendMessage(ChatColor.BLUE + "Note: to reset everything, end the poll and reopen it with the /poll command.");
                break;
            case 2:
                player.sendMessage(ChatColor.GREEN + "Registered first choice: " + ChatColor.AQUA + message);
                player.sendMessage(ChatColor.GREEN + "Now, please enter the second option/choice of your poll.");
                break;
            case 3:
                player.sendMessage(ChatColor.GREEN + "Registered second choice: " + ChatColor.AQUA + message);
                player.sendMessage(ChatColor.GREEN + "You have now completed the mandatory part of your poll. If you wish to add more options, " +
                "type them like you did now. If you wish to finish, type " + ChatColor.AQUA + "" + ChatColor.BOLD + "\"end\"" +
                ChatColor.RESET + "" + ChatColor.GREEN + ". If you wish to reset everything, end the poll and reopen it with the /poll command.");
                break;
            case 4:
                player.sendMessage(ChatColor.GREEN + "Registered another choice: " + ChatColor.AQUA + message);
                player.sendMessage(ChatColor.GREEN + "If you wish to add more options, type them like you did now. If you wish to finish, type " +
                        ChatColor.AQUA + "" + ChatColor.BOLD + "\"end\"" + ChatColor.RESET + "" + ChatColor.GREEN + ".");
                break;
        }
        player.sendMessage(ChatColor.YELLOW + "--------------------------------------------------------------");
        doSound();
        if (pollStage < 4)  {
            Listener_Chat.pollStage = pollStage + 1;
        }
         */
    }

}
