package net.pixlies.core.entity.data;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class UserSettings {

    private boolean inStaffMode;
    private boolean bypassing;
    private boolean vanished;
    private boolean passive;

    public static UserSettings getDefaults() {
        return new UserSettings(
                false,
                false,
                false,
                false
        );
    }

}
