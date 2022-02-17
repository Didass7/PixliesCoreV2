package net.pixlies.core.entity.data;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.Main;
import net.pixlies.core.house.House;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Entity
public class UserStats {

    private String dateJoined; // In Pixlies DateAndTime
    private Map<House, Integer> houses; // House & House XPs
    private int civilPoints; // Range: -100 to 100

    public static UserStats createNew() {
        return new UserStats(
                Main.getInstance().getCalendar().formatDateAndTime(),
                new HashMap<>(){{
                    for (House house : House.values())
                        put(house, 0);
                }},
                0
        );
    }

    public House getHouse() {
        int max = Collections.max(houses.values());

        if (max < 200) return House.NOT_DECIDED;

        for (Map.Entry<House, Integer> entry : houses.entrySet()) {
            if (entry.getValue()==max) {
                return entry.getKey();
            }
        }

        return House.NOT_DECIDED;
    }

}
