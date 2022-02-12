/**
 * @author vPrototype_
 */

package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pixlies.core.Main;
import net.pixlies.core.entity.Poll;
import net.pixlies.core.handlers.impl.PollHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Description("Lists all the active poll ids")
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
        // Checks if the poll id is valid
        if (!pollHandler.getPolls().containsKey(id)) {
            Lang.POLL_DOES_NOT_EXIST.send(sender);
            return;
        }
        
        pollHandler.getPolls().get(id).end();
    }

    @Subcommand("view")
    @Description("View an active poll")
    public void onPollView(Player player, String id) {
        // Checks if the poll id is valid
        if (!pollHandler.getPolls().containsKey(id)) {
            Lang.POLL_DOES_NOT_EXIST.send(player);
            return;
        }

        List<Integer> voteKeys = pollHandler.getPolls().get(id).getPollVotes().keySet().stream().toList();

        Lang.POLL_VIEW_INFO.send(player, "%ID%;" + id);
        for (int i : pollHandler.getPolls().get(id).getPollVotes().keySet()) {
            TextComponent component = Component.text((i + 1) + ". ", NamedTextColor.GOLD)
                    .append(Component.text(voteKeys.indexOf(i), NamedTextColor.GRAY)
                            .hoverEvent(HoverEvent.showText(Component.text("Click here to vote for this option!", NamedTextColor.GOLD)))
                            .clickEvent(ClickEvent.suggestCommand("poll vote " + id + " " + (i + 1))));
            player.sendMessage(component);
        }
    }

    @Subcommand("vote")
    @Description("Cast a vote on an active poll")
    public void onPollVote(Player player, String id, int option) {
        // Checks if the poll id is valid
        if (!pollHandler.getPolls().containsKey(id)) {
            Lang.POLL_DOES_NOT_EXIST.send(player);
            return;
        }

        // Checks if the option is valid
        if (!pollHandler.getPolls().get(id).getPollVotes().containsKey(option)) {
            Lang.POLL_OPTION_DOES_NOT_EXIST.send(player);
            return;
        }

        pollHandler.getPolls().get(id).registerVote(player.getUniqueId(), option);
        Lang.REGISTERED_VOTE.send(player, "%OPTION%;" + option);
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 100, 1);
    }

    @Subcommand("results")
    @Description("View the results of a previous poll")
    public void onPollResults(Player player, String id) {
        // Checks if the poll id is valid
        if (!pollHandler.getPolls().containsKey(id)) {
            Lang.POLL_DOES_NOT_EXIST.send(player);
            return;
        }

        Map<Integer, Integer> votes = pollHandler.getPolls().get(id).getPollVotes();
        List<Integer> voteKeys = votes.keySet().stream().toList();
        Multimap<Integer, UUID> voters = pollHandler.getPolls().get(id).getVoterInfo();

        Lang.POLL_RESULTS_INFO.send(player, "%ID%;" + id);
        for (int i : votes.keySet()) {
            TextComponent hoverText = Component.text("");
            for (UUID uuid : voters.values()) {
                Player p = (Player) Bukkit.getOfflinePlayer(uuid);
                if (p.isOnline()) {
                    hoverText.append(Component.text(p.getName(), NamedTextColor.GOLD));
                } else {
                    hoverText.append(Component.text(p.getName(), NamedTextColor.GRAY));
                }
                if (voters.values().stream().toList().get(voters.size() - 1) == uuid) {
                    hoverText.append(Component.text(", ", NamedTextColor.GRAY));
                }
            }

            TextComponent component = Component.text((i + 1) + ". ", NamedTextColor.GOLD)
                    .append(Component.text(voteKeys.indexOf(i), NamedTextColor.GRAY).hoverEvent(HoverEvent.showText(hoverText)));
            player.sendMessage(component);
        }
    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
