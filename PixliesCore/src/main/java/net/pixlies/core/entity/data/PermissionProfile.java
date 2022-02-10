package net.pixlies.core.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PermissionProfile {

    private List<String> groups;
    private List<String> individualPermissions;

}
