/**
 * @author vPrototype_
 */

package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.Poll;
import net.pixlies.core.handlers.impl.PollHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("poll|polls")
@CommandPermission("pixlies.cosmetics.poll")
public class PollCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final PollHandler pollHandler = instance.getHandlerManager().getHandler(PollHandler.class);

    @Subcommand("create")
    @Description("Create a poll")
    public void onPollCreate(Player player, String question) {
        Poll poll = new Poll(player.getUniqueId(), question);
        pollHandler.getPollsInCreation().put(player.getUniqueId(), poll);
        Lang.CREATING_POLL.send(player, "%QUESTION%;" + question);
        Lang.ENTER_POLL_OPTION.send(player, "%NUMBER%;" + "first");
        Lang.HOW_TO_CANCEL_POLL.send(player);
    }

    @Subcommand("list")
    @Description("Lists all the active poll ids.")
    public void onPollList(CommandSender sender) {
        List<Poll> polls = pollHandler.getPolls().values().stream().toList();
        sender.sendMessage(Lang.POLL);
        for (Poll poll : pollHandler.getPolls().values()) {
            String status;
            if (poll.getStage() == 2) {
                status = "§cENDED§7)";
            } else {
                status = "§aACTIVE§7)";
            }

            String creator = Bukkit.getOfflinePlayer(poll.getPollCreator()).getName();
            sender.sendMessage("  §7" + polls.indexOf(poll) + ". §d" + poll.getId() + "§7: §f" + poll.getPollInfo().get(0) +
                    " §7(by §6" + creator + "§7, " + status);
        }
    }

    @Subcommand("end")
    @Description("End an active poll")
    public void onPollEnd(CommandSender sender, String id) {

    }

    @Subcommand("view")
    @Description("View an active poll")
    public void onPollView(Player player) {

    }

    @Subcommand("results")
    @Description("View the results of the previous poll")
    public void onPollResults(Player player) {

    }

    @Subcommand("vote")
    @Description("Cast a vote on an active poll")
    public void onPollVote(Player player) {

    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
