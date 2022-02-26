package net.pixlies.core.entity.data;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.scoreboard.ScoreboardAdapter.ScoreboardType;

@Data
@Entity
@AllArgsConstructor
public class UserPersonalization {

    private boolean commandSpyEnabled;
    private boolean socialSpyEnabled;
    private boolean viewMutedChat;
    private boolean viewBannedJoins;
    private boolean bypassClearChat;
    private boolean joinVanish;
    private String scoreboardType;

    public ScoreboardType getScoreboardTypeAsEnum() {
        return ScoreboardType.valueOf(scoreboardType);
    }

    public static UserPersonalization getDefaults() {
        return new UserPersonalization(
                false,
                false,
                false,
                false,
                false,
                false,
                ScoreboardType.STANDARD.name()
        );
    }

}
