package net.pixlies.core.modules;

import lombok.Data;
import org.bukkit.Material;

@Data
public class ModuleDescription {

    private final String name;
    private final String[] authors;
    private final String version;
    private final String main;
    private final String icon;
    private boolean activated;

    public ModuleDescription(String name, String[] authors, String version, String main, String icon) {
        this.name = name;
        this.authors = authors;
        this.version = version;
        this.main = main;
        this.icon = icon;
        activated = true;
    }

    public Material getIcon() {
        return Material.getMaterial(icon);
    }

    /**
     *  Do not set this from the description, use the module manager's methods instead.
     * @param activated Set the activation status.
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

}
