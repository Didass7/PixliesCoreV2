package net.pixlies.core.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class UserSettings {

    private static final String STAFF_MODE_ENABLED = "staffMode";

    private boolean staffModeEnabled;

    public UserSettings(Map<String, Object> map) {
        this.staffModeEnabled = (boolean) map.get(STAFF_MODE_ENABLED);
    }

    public static UserSettings getFromMongo(Map<String, Object> map) {
        return new UserSettings(map);
    }

    public Map<String, Object> mapForMongo() {
        return new HashMap<>() {{
            put(STAFF_MODE_ENABLED, staffModeEnabled);
        }};
    }

    public static UserSettings getDefaults() {
        return new UserSettings(
                false
        );
    }

}
