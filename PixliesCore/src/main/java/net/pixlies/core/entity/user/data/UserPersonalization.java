package net.pixlies.core.entity.user.data;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.scoreboard.ScoreboardType;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Gets the scoreboard type as an enum.
     * @see User#getScoreboardType()
     * @return The scoreboard type of the user.
     */
    public @Nullable ScoreboardType getScoreboardTypeAsEnum() {
        if (scoreboardType == null) return null;
        try {
            return ScoreboardType.valueOf(scoreboardType);
        } catch (IllegalArgumentException ignored) {

        }
        return null;
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
