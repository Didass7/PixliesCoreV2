package net.pixlies.core.entity.data;

import dev.morphia.annotations.Entity;
import lombok.Data;

@Data
@Entity
public class UserStats {

    // IN PIXLIES TIME
    private String dateJoined;

}
