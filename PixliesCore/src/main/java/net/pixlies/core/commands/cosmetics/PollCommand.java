package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.Poll;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("poll|polls")
@CommandPermission("pixlies.cosmetics.poll")
public class PollCommand extends BaseCommand {

    @Subcommand("create")
    @Description("Create a poll")
    public void onPollCreate(Player player, String question) {
        Poll poll = new Poll(player.getUniqueId(), question);
        Lang.CREATING_POLL.send(player, "%QUESTION%;" + question);
    }

    @Subcommand("end")
    @Description("End an active poll")
    public void onPollEnd(Player player) {

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
