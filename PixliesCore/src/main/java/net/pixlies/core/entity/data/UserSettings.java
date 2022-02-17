package net.pixlies.core.entity.data;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Entity
public class UserSettings {

    private boolean inStaffMode;
    private boolean hasNationBypass;
    private boolean vanished;
    private boolean passive;

    public UserSettings(Map<String, Object> map) {
        this.inStaffMode = (boolean) map.get(STAFF_MODE_ENABLED);
        this.hasNationBypass = (boolean) map.get(NATIONS_BYPASS_ENABLED);
        this.vanished = (boolean) map.get(VANISHED);
        this.passive = (boolean) map.get(PASSIVE);
    }

    public static final String STAFF_MODE_ENABLED = "staffMode";
    public static final String NATIONS_BYPASS_ENABLED = "hasNationBypass";
    public static final String VANISHED = "vanished";
    public static final String PASSIVE = "passive";

    public static UserSettings getFromMongo(Map<String, Object> map) {
        return new UserSettings(map);
    }

    public boolean hasNationBypass() {
        return hasNationBypass;
    }

    public Map<String, Object> mapForMongo() {
        return new HashMap<>() {{
            put(STAFF_MODE_ENABLED, inStaffMode);
            put(NATIONS_BYPASS_ENABLED, hasNationBypass);
            put(VANISHED, vanished);
            put(PASSIVE, passive);
        }};
    }

    public static UserSettings getDefaults() {
        return new UserSettings(
                false,
                false,
                false,
                false
        );
    }

}
