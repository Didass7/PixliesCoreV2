/**
 * @author vPrototype_
 */

package net.pixlies.core.entity;

import lombok.Getter;
import net.pixlies.core.utils.TextUtils;

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
    private final List<String> pollInfo;

    /**
     * Stores the votes of the poll.
     * First identifier is the index of the answer in pollInfo, minus 1.
     * Second identifier is the amount of votes the poll has.
     */
    private final Map<Integer, Integer> pollVotes;

    /**
     * Stores which UUIDs voted for which option.
     */
    private final Map<UUID, Integer> voterInfo;

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
        voterInfo = new HashMap<>();
        pollInfo.add(question);
        stage = 0;
        id = TextUtils.generateId(7);
    }

    public void addAnswer(String answer) {
        pollInfo.add(answer);
    }

    public void removeVote(UUID voter, int option) {
        if (voterInfo.containsKey(voter)) {
            pollVotes.put(option - 1, pollVotes.get(option - 1) - 1);
            voterInfo.put(voter, 0);
        }
    }

    public void registerVote(UUID voter, int option) {
        pollVotes.put(option - 1, pollVotes.get(option - 1) + 1);
        voterInfo.put(voter, option);
    }

    public void makePublic() {
        stage = 1;
    }

    public void end() {
        // If an option got no votes, puts its value as 0 in pollVotes
        for (int i = 1; i <= pollInfo.size(); i++) {
            if (!pollVotes.containsKey(i)) {
                pollVotes.put(i - 1, 0);
            }
        }

        stage = 2;
    }

}
