/**
 * @author vPrototype_
 */

package net.pixlies.core.entity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.PollHandler;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Represents a Poll object
 *
 * @author vPrototype_
 */
public class Poll {

    /**
     * UUID of the player who created the poll.
     */
    @Getter private final UUID pollCreator;

    /**
     * Poll question and its possible answers.
     * Index 0 is the question, and the rest are the answers.
     */
    @Getter private final List<String> pollInfo;

    /**
     * Votes of the poll.
     * First identifier is the index of the answer in pollInfo, minus 1.
     * Second identifier is the amount of votes the poll has.
     */
    @Getter private final Map<Integer, Integer> pollVotes;

    /**
     * Which UUIDs voted for which option.
     * First identifier is the index of the answer in pollInfo, minus 1.
     * Second identifier is the list of UUIDs who voted for the option.
     */
    @Getter private final Multimap<Integer, UUID> voterInfo;

    /**
     * Stage of the poll.
     * 0: the poll is in construction.
     * 1: the poll is active.
     * 2: the poll has ended.
     */
    @Getter private byte stage;

    /**
     * ID of the poll
     */
    @Getter private final String id;

    private static final Main instance = Main.getInstance();

    public Poll(UUID pollCreator, String question) {
        this.pollCreator = pollCreator;
        pollInfo = new ArrayList<>();
        pollVotes = new HashMap<>();
        voterInfo = ArrayListMultimap.create();
        pollInfo.add(question);
        stage = 0;
        id = TextUtils.generateId(7);
    }

    public void addAnswer(String answer) {
        pollInfo.add(answer);
    }

    public void registerVote(UUID voter, int option) {
        if (voterInfo.containsKey(voter)) {
            pollVotes.put(option - 1, pollVotes.get(option - 1) - 1);
        }
        pollVotes.put(option - 1, pollVotes.get(option - 1) + 1);
        voterInfo.put(option - 1, voter);
    }

    public void makePublic() {
        stage = 1;

        TextComponent component = new TextComponent("  " + Lang.POLL);

        TextComponent text = new TextComponent("Poll started! View with ");
        text.setColor(ChatColor.GRAY);
        component.addExtra(text);

        TextComponent view = new TextComponent("/poll view " + id);
        view.setColor(ChatColor.GREEN);
        view.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click here to view the poll!").color(ChatColor.GRAY).create()));
        view.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "poll view " + id));
        component.addExtra(view);

        TextComponent dot = new TextComponent(".");
        dot.setColor(ChatColor.GRAY);
        component.addExtra(dot);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(component);
        }
    }

    public void end() {
        // If an option got no votes, puts its value as 0 in pollVotes
        for (int i = 1; i <= pollInfo.size(); i++) {
            if (!pollVotes.containsKey(i)) {
                pollVotes.put(i - 1, 0);
            }
        }

        stage = 2;

        TextComponent component = new TextComponent("  " + Lang.POLL);

        TextComponent text = new TextComponent("Poll ended! View results with ");
        text.setColor(ChatColor.GRAY);
        component.addExtra(text);

        TextComponent results = new TextComponent("/poll results " + id);
        results.setColor(ChatColor.GREEN);
        results.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click here to view the ended poll!").color(ChatColor.GOLD).create()));
        results.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "poll results " + id));
        component.addExtra(results);

        TextComponent dot = new TextComponent(".");
        dot.setColor(ChatColor.GOLD);
        component.addExtra(dot);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(component);
        }
    }

    /**
     * Displays info about the poll
     * @param results true if displaying results, false if displaying regularly
     * @param player player displaying info to
     */
    public void displayInfo(boolean results, Player player) {
        List<String> options = pollInfo.subList(1, pollInfo.size() - 1);

        Lang.POLL_SHOW_QUESTION.send(player, "%QUESTION%;" + pollInfo.get(0));
        for (int i = 0; i < pollInfo.size(); i++) {
            TextComponent component = new TextComponent("  " + Lang.POLL);

            TextComponent number = new TextComponent((i + 1) + ". ");
            number.setColor(ChatColor.GREEN);
            component.addExtra(number);

            TextComponent option = new TextComponent(options.get(i));

            if (results) {
                TextComponent comma = new TextComponent(", ");
                comma.setColor(ChatColor.GRAY);

                for (int i2 : pollVotes.keySet()) {
                    TextComponent hoverText = new TextComponent("");
                    for (UUID uuid : voterInfo.values()) {
                        Player p = (Player) Bukkit.getOfflinePlayer(uuid);
                        TextComponent name = new TextComponent(p.getName());

                        if (p.isOnline()) name.setColor(ChatColor.GREEN);
                        else name.setColor(ChatColor.GRAY);

                        if (voterInfo.values().stream().toList().get(voterInfo.size() - 1) == uuid) hoverText.addExtra(comma);

                        hoverText.addExtra(name);
                    }
                    
                    // TODO: set color of the option (green if its the most voted, gray if it isn't)

                    option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
                }
            } else {
                option.setColor(ChatColor.GREEN);
                option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click here to vote for this option!").color(ChatColor.GOLD).create()));
                option.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "poll vote " + id + " " + (i + 1)));
                component.addExtra(option);
            }

            player.sendMessage(component);
        }
    }

    public static boolean pollExists(String id, UUID uuid) {
        PollHandler handler = instance.getHandlerManager().getHandler(PollHandler.class);

        if (!handler.getPolls().containsKey(id)) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            Lang.POLL_DOES_NOT_EXIST.send(player, "%ID%;" + id);
            player.playSound(player.getLocation(), "entity.villager.no", 100, 1);
            return false;
        }

        return true;
    }

}
