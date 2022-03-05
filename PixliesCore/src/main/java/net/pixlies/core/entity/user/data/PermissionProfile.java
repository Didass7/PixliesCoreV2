package net.pixlies.core.entity.user.data;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
public class PermissionProfile {

    private List<String> groups;
    private List<String> individualPermissions;

}
