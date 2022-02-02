package net.pixlies.core.entity.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class UserPersonalization {

    private static final String SOCIAL_SPY_ENABLED = "socialSpyEnabled";
    private static final String COMMAND_SPY_ENABLED = "commandSpyEnabled";
    private static final String VIEW_MUTED_CHAT = "viewMutedChat";

    private boolean socialSpyEnabled;
    private boolean commandSpyEnabled;
    @Getter(AccessLevel.NONE) private boolean viewMutedChat;

    public boolean canViewMutedChat() {
        // lombok wasn't being fun
        return viewMutedChat;
    }

    public UserPersonalization(Map<String, Object> map) {
        this.socialSpyEnabled = (boolean) map.get(SOCIAL_SPY_ENABLED);
        this.commandSpyEnabled = (boolean) map.get(COMMAND_SPY_ENABLED);
        this.viewMutedChat = (boolean) map.get(VIEW_MUTED_CHAT);
    }

    public static UserPersonalization getFromMongo(Map<String, Object> map) {
        return new UserPersonalization(map);
    }

    public Map<String, Object> mapForMongo() {
        return new HashMap<>() {{
            put(SOCIAL_SPY_ENABLED, socialSpyEnabled);
            put(COMMAND_SPY_ENABLED, commandSpyEnabled);
            put(VIEW_MUTED_CHAT, viewMutedChat);
        }};
    }

    public static UserPersonalization getDefaults() {
        return new UserPersonalization(
                false,
                false,
                false
        );
    }

}
