/**
 * @author vPrototype_
 */

package net.pixlies.core.entity.polls;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.PollHandler;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Poll object
 *
 * @author vPrototype_
 * @author Dynmie
 */
@ToString
@EqualsAndHashCode
public class Poll {

    @Getter private final String question;

    /**
     * Poll question and its possible answers.
     */
    @Getter private final List<PollAnswer> answers = new ArrayList<>(); // QUESTION

    /**
     * List of all voted players.
     */
    @Getter private final List<UUID> votedPlayers = new ArrayList<>();

    /**
     * ID of the poll
     */
    @Getter private final String id = TextUtils.generateId(32);

    @Getter private boolean started;

    private static final Main instance = Main.getInstance();
    private static final PollHandler handler = instance.getHandlerManager().getHandler(PollHandler.class);

    public Poll(String question) {
        this.question = question;
    }

    public void addAnswer(String answer) {
        answers.add(new PollAnswer(answer, 0));
    }

    public void registerVote(UUID voter, int option) {
        if (votedPlayers.contains(voter)) return;
        PollAnswer answer = answers.get(option);
        if (answer == null) return;
        answer.setVotes(answer.getVotes() + 1);
        votedPlayers.add(voter);
    }

    public boolean doesAnswerExist(int index) {
        if (index < 0) return false;
        if (index > answers.size()) return false;
        return answers.get(index) != null;
    }

    public void start() {
        this.started = true;
        instance.getServer().getScheduler().runTaskLater(instance, this::end, 600); // 30s x 20tps

        String header = Lang.POLL_QUESTION_HEADER.get()
                .replace("%PREFIX%", Lang.POLL)
                .replace("%QUESTION%", question);

        List<Component> lines = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            PollAnswer answer = answers.get(i);
            Component component = Component.text(Lang.POLL_QUESTION_LINE.get()
                            .replace("%INDEX%", (i + 1) + "")
                            .replace("%ANSWER%", answer.getAnswer())) // to component here
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/poll vote " + (i + 1)))
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(CC.format("&aClick to select this answer."))));
            lines.add(component);
        }

        String footer = Lang.POLL_QUESTION_FOOTER.get()
                .replace("%PREFIX%", Lang.POLL)
                .replace("%QUESTION%", question);

        Bukkit.broadcastMessage(header);
        for (Component component : lines) {
            Bukkit.broadcast(component);
        }
        Bukkit.broadcastMessage(footer);
    }

    public void end() {
        Poll activePoll = handler.getActivePoll();
        if (activePoll != this) return;
        if (!activePoll.isStarted()) return;
        this.started = false;
        handler.setActivePoll(null);

        String header = Lang.POLL_RESULT_HEADER.get()
                .replace("%PREFIX%", Lang.POLL)
                .replace("%TOTAL%", votedPlayers.size() + "")
                .replace("%QUESTION%", question);

        List<String > lines = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            PollAnswer answer = answers.get(i);
            lines.add(Lang.POLL_RESULT_LINE.get()
                    .replace("%INDEX%", (i + 1) + "")
                    .replace("%ANSWER%", answer.getAnswer())
                    .replace("%VOTES%", answer.getVotes() + ""));
        }

        String footer = Lang.POLL_RESULT_FOOTER.get()
                .replace("%PREFIX%", Lang.POLL)
                .replace("%TOTAL%", votedPlayers.size() + "")
                .replace("%QUESTION%", question);

        Bukkit.broadcastMessage(header);
        for (String message : lines) {
            Bukkit.broadcastMessage(message);
        }
        Bukkit.broadcastMessage(footer);
    }

}
