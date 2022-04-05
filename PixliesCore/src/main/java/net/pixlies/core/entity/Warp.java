package net.pixlies.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.utils.location.LazyLocation;
import org.bukkit.Bukkit;
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
    private static final Config config = instance.getWarpsConfig();

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
        return new Warp(
                name,
                section.getString("description", "No description."),
                Material.valueOf(section.getString("material", "BARRIER")),
                section.getLocation("location", new Location(Bukkit.getWorld("world"), 0, 0, 0))
        );
    }

    public static Warp getSpawn() {
        Warp spawn = get(config.getString("warps.spawn.name"));
        if (spawn == null) {
            spawn = new Warp(
                    config.getString("warps.spawn.name", "Spawn"),
                    config.getString("warps.spawn.description", "No description."),
                    Material.valueOf(config.getString("warps.spawn.material", "DIAMOND_SWORD")),
                    new Location(Bukkit.getWorld("world"), 0, 0, 0)
            );
            spawn.save();
        }
        return spawn;
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
                    section.getString("description", "No description."),
                    Material.valueOf(section.getString("material", "BARRIER")),
                    section.getLocation("location", new Location(Bukkit.getWorld("world"), 0, 0, 0))
            ));
        });
        return warps;
    }

}
