package net.pixlies.core.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class UserSettings {

    private static final String STAFF_MODE_ENABLED = "staffMode";
    private static final String VANISHED = "vanished";
    private static final String PASSIVE = "passive";

    private boolean inStaffMode;
    private boolean vanished;
    private boolean passive;

    public UserSettings(Map<String, Object> map) {
        this.inStaffMode = (boolean) map.get(STAFF_MODE_ENABLED);
        this.vanished = (boolean) map.get(VANISHED);
        this.passive = (boolean) map.get(PASSIVE);
    }

    public static UserSettings getFromMongo(Map<String, Object> map) {
        return new UserSettings(map);
    }

    public Map<String, Object> mapForMongo() {
        return new HashMap<>() {{
            put(STAFF_MODE_ENABLED, inStaffMode);
            put(VANISHED, vanished);
            put(PASSIVE, passive);
        }};
    }

    public static UserSettings getDefaults() {
        return new UserSettings(
                false,
                false,
                false
        );
    }

}
