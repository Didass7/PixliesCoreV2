package net.pixlies.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.utils.location.LazyLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

/**
 * Warps
 * @author Dynmie
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class Warp extends LazyLocation {

    private static final Main instance = Main.getInstance();
    private static final Config config = instance.getConfig();

    private final String name;
    private final String description;
    private final Material material;

    public Warp(String name, String description, Material material, Location location) {
        super(location);
        this.name = name;
        this.description = description;
        this.material = material;
    }


    public void save() {
        ConfigurationSection section = config.getConfigurationSection(name);
        if (section == null)
            section = config.createSection(name);
        section.set("description", description);
        section.set("material", material.name());
        section.set("location", getAsBukkitLocation());
    }

    public static Warp get(String name) {
        ConfigurationSection section = config.getConfigurationSection(name);
        if (section == null) return null;
        Location location = section.getLocation("location");
        return new Warp(
                name,
                section.getString("description"),
                Material.valueOf(section.getString("material")),
                location
        );
    }

    public static Collection<Warp> getWarps() {
        Set<String> keys = config.getKeys(false);
        if (keys.isEmpty()) return Collections.emptyList();
        List<Warp> warps = new ArrayList<>();
        keys.forEach(key -> {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null) return;
            warps.add(new Warp(
                    key,
                    section.getString("description"),
                    Material.valueOf(section.getString("material")),
                    section.getLocation("location")
            ));
        });
        return warps;
    }

}
