/**
 * @author vPrototype_
 */

package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.polls.Poll;
import net.pixlies.core.handlers.impl.PollHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command for managing and viewing polls
 *
 * @author vPrototype_
 * @author Dynmie
 */
@CommandAlias("poll|polls")
public class PollCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final PollHandler pollHandler = instance.getHandlerManager().getHandler(PollHandler.class);

    @Subcommand("create")
    @Description("Create a poll")
    @CommandPermission("pixlies.staff.poll")
    public void onPollCreate(CommandSender sender, String question) {
        Poll poll = pollHandler.getActivePoll();
        if (poll != null) {
            if (poll.isStarted()) {
                Lang.POLL_ALREADY_STARTED.send(sender);
            } else {
                Lang.POLL_ALREADY_CREATING.send(sender);
            }
            return;
        }

        pollHandler.setActivePoll(new Poll(question));
        Lang.POLL_CREATED.send(sender, "%QUESTION%;" + question);
    }

    @Subcommand("end")
    @Description("End an active poll")
    @CommandPermission("pixlies.staff.poll")
    public void onPollEnd(CommandSender sender) {
        Poll poll = pollHandler.getActivePoll();
        if (poll == null || !poll.isStarted()) {
            Lang.POLL_NOT_EXIST.send(sender);
            return;
        }

        poll.end();
    }

    @Subcommand("delete")
    @Description("Delete a poll")
    @CommandPermission("pixlies.staff.poll")
    public void onPollDelete(CommandSender sender) {
        Poll poll = pollHandler.getActivePoll();
        if (poll == null) {
            Lang.POLL_NOT_EXIST.send(sender);
            return;
        }

        if (poll.isStarted()) {
            Lang.POLL_ALREADY_STARTED.send(sender);
            return;
        }

        pollHandler.setActivePoll(null);
        Lang.POLL_DELETED.send(sender);
    }

    @Subcommand("addanswer")
    @Syntax("<answer>")
    @Description("Add a poll answer")
    @CommandPermission("pixlies.staff.poll")
    public void onPollAnswerAdd(CommandSender sender, String answer) {
        Poll poll = pollHandler.getActivePoll();
        if (poll == null) {
            Lang.POLL_NOT_EXIST.send(sender);
            return;
        }

        if (poll.isStarted()) {
            Lang.POLL_ALREADY_STARTED.send(sender);
            return;
        }

        poll.addAnswer(answer);
        Lang.POLL_ADD_ANSWER.send(sender);
    }

    @Subcommand("start")
    @Description("Start the poll")
    @CommandPermission("pixlies.staff.poll")
    public void onPollStart(CommandSender sender) {
        Poll poll = pollHandler.getActivePoll();
        if (poll == null) {
            Lang.POLL_NOT_EXIST.send(sender);
            return;
        }

        if (poll.isStarted()) {
            Lang.POLL_ALREADY_STARTED.send(sender);
            return;
        }

        if (poll.getAnswers().size() < 2) {
            Lang.POLL_NOT_ENOUGH_ANSWERS.send(sender);
            return;
        }

        poll.start();
    }

    @Subcommand("vote")
    @Syntax("<option>")
    @Description("Cast a vote on an active poll")
    public void onPollVote(Player player, int option) {
        Poll poll = pollHandler.getActivePoll();
        if (poll == null || !poll.isStarted()) {
            Lang.POLL_NOT_EXIST.send(player);
            return;
        }

        // already voted
        if (poll.getVotedPlayers().contains(player.getUniqueId())) {
            Lang.POLL_ALREADY_VOTED.send(player);
            return;
        }

        if (!poll.doesAnswerExist(option - 1)) {
            Lang.POLL_OPTION_DOES_NOT_EXIST.send(player);
            return;
        }

        poll.registerVote(player.getUniqueId(), option - 1);
        Lang.POLL_REGISTERED_VOTE.send(player, "%OPTION%;" + option);
    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
