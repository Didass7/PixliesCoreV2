package net.pixlies.core.entity.data;

import dev.morphia.annotations.Entity;
import lombok.Data;
import net.pixlies.core.commands.staff.StaffSettingsCommand;
import net.pixlies.core.scoreboard.ScoreboardAdapter.ScoreboardType;

import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class UserPersonalization {

    private boolean commandSpyEnabled;
    private boolean socialSpyEnabled;
    private boolean viewMutedChat;
    private boolean viewBannedJoins;
    private boolean bypassClearChat;
    private boolean joinVanish;
    private String scoreboardType;

    public static final String COMMAND_SPY_ENABLED = "commandSpyEnabled";
    public static final String SOCIAL_SPY_ENABLED = "socialSpyEnabled";
    public static final String VIEW_MUTED_CHAT = "viewMutedChat";
    public static final String VIEW_BANNED_JOINS = "viewBannedJoins";
    public static final String BYPASS_CLEAR_CHAT = "bypassClearChat";
    public static final String JOIN_VANISH = "joinVanish";
    public static final String SCOREBOARD_TYPE = "scoreboardType";

    public boolean canViewMutedChat() {
        // lombok wasn't being fun
        return viewMutedChat;
    }

    public boolean canViewBannedJoins() {
        // lombok wasn't being fun
        return viewBannedJoins;
    }

    public boolean canBypassClearchat() {
        // lombok wasn't being fun
        return bypassClearChat;
    }

    public UserPersonalization(Map<String, Object> map) {
        this.commandSpyEnabled = (boolean) map.get(COMMAND_SPY_ENABLED);
        this.socialSpyEnabled = (boolean) map.get(SOCIAL_SPY_ENABLED);
        this.viewMutedChat = (boolean) map.get(VIEW_MUTED_CHAT);
        this.viewBannedJoins = (boolean) map.get(VIEW_BANNED_JOINS);
        this.bypassClearChat = (boolean) map.get(BYPASS_CLEAR_CHAT);
        this.joinVanish = (boolean) map.get(JOIN_VANISH);
        this.scoreboardType = (String) map.get(SCOREBOARD_TYPE);
    }

    public UserPersonalization(boolean commandSpyEnabled, boolean socialSpyEnabled, boolean viewMutedChat, boolean viewBannedJoins, boolean bypassClearChat, boolean joinVanish, String scoreboardType) {
        this.commandSpyEnabled = commandSpyEnabled;
        this.socialSpyEnabled = socialSpyEnabled;
        this.viewMutedChat = viewMutedChat;
        this.viewBannedJoins = viewBannedJoins;
        this.bypassClearChat = bypassClearChat;
        this.joinVanish = joinVanish;
        this.scoreboardType = scoreboardType;
    }

    public boolean isSettingEnabled(StaffSettingsCommand.StaffSetting s) {
        return switch (s) {
            case COMMANDSPY -> commandSpyEnabled;
            case SOCIALSPY -> socialSpyEnabled;
            case BANSPY -> viewBannedJoins;
            case MUTESPY -> viewMutedChat;
            case BYPASSCLEARCHAT -> bypassClearChat;
        };
    }

    public ScoreboardType getScoreboardTypeAsEnum() {
        return ScoreboardType.valueOf(scoreboardType);
    }

    public static UserPersonalization getFromMongo(Map<String, Object> map) {
        return new UserPersonalization(map);
    }

    public Map<String, Object> mapForMongo() {
        return new HashMap<>() {{
            put(COMMAND_SPY_ENABLED, commandSpyEnabled);
            put(SOCIAL_SPY_ENABLED, socialSpyEnabled);
            put(VIEW_MUTED_CHAT, viewMutedChat);
            put(VIEW_BANNED_JOINS, viewBannedJoins);
            put(BYPASS_CLEAR_CHAT, bypassClearChat);
            put(JOIN_VANISH, joinVanish);
            put(SCOREBOARD_TYPE, scoreboardType);
        }};
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
