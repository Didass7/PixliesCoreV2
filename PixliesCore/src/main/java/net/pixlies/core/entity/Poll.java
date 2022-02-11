/**
 * @author vPrototype_
 */

package net.pixlies.core.entity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Poll {

    /**
     * UUID of the player who created the poll.
     */
    @Getter private final UUID pollCreator;

    /**
     * Stores the poll question and its possible answers.
     * Index 0 is the question, and the rest are the answers.
     */
    @Getter private final List<String> pollInfo;

    /**
     * Stores the votes of the poll.
     * First identifier is the index of the answer in pollInfo, minus 1.
     * Second identifier is the amount of votes the poll has.
     */
    @Getter private final Map<Integer, Integer> pollVotes;

    /**
     * Stores which UUIDs voted for which option.
     * First identifier is the index of the answer in pollInfo, minus 1.
     * Second identifier is the list of UUIDs who voted for the option.
     */
    @Getter private final Multimap<Integer, UUID> voterInfo;

    /**
     * Stores the stage of the poll.
     * 0: the poll is in construction.
     * 1: the poll is active.
     * 2: the poll has ended.
     */
    @Getter private byte stage;

    @Getter private final String id;

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

        TextComponent component = Component.text("  " + Lang.POLL)
                .append(Component.text("Poll started! View with ", NamedTextColor.GRAY))
                .append(Component.text("/poll view " + id, NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text("Click here to view the poll!", NamedTextColor.GOLD)))
                        .clickEvent(ClickEvent.suggestCommand("poll view " + id)))
                .append(Component.text(".", NamedTextColor.GRAY));

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

        TextComponent component = Component.text("  " + Lang.POLL)
                .append(Component.text("Poll ended! View results with ", NamedTextColor.GRAY))
                .append(Component.text("/poll results " + id, NamedTextColor.RED)
                        .hoverEvent(HoverEvent.showText(Component.text("Click here to view the ended poll!", NamedTextColor.GOLD)))
                        .clickEvent(ClickEvent.suggestCommand("poll results " + id)))
                .append(Component.text(".", NamedTextColor.GRAY));

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(component);
        }
    }

}
