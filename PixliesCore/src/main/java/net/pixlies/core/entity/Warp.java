package net.pixlies.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.utils.TextUtils;
import net.pixlies.core.utils.location.LazyLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private static final Config warpsConfig = instance.getWarpsConfig();

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
        ConfigurationSection section = warpsConfig.getConfigurationSection(name);
        if (section == null)
            section = warpsConfig.createSection(UUID.randomUUID().toString());
        section.set("name", name);
        section.set("description", description);
        section.set("material", material.name());
        section.set("location", this.getAsBukkitLocation());
        warpsConfig.save();
    }

    public static Warp get(String name) {
        for (Warp warp : Warp.getWarps()) {
            if (!warp.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return warp;
        }
        return null;
    }

    public static @NotNull Warp getSpawn() {
        Warp spawn = get(config.getString("warps.spawn.name", "Spawn"));
        if (spawn == null) {
            spawn = new Warp(
                    config.getString("warps.spawn.name", "Spawn"),
                    config.getString("warps.spawn.description", "No description."),
                    Material.valueOf(config.getString("warps.spawn.material", "BEDROCK")),
                    new Location(Bukkit.getWorlds().get(0), 0, 64, 0)
            );
            spawn.save();
        }
        return spawn;
    }

    public static @NotNull Collection<Warp> getWarps() {
        Set<String> keys = warpsConfig.getKeys(false);
        if (keys.isEmpty()) return Collections.emptyList();
        List<Warp> warps = new ArrayList<>();
        keys.forEach(key -> {

            ConfigurationSection section = warpsConfig.getConfigurationSection(key);
            if (section == null) return;

            String name = section.getString("name");
            if (name == null) return;
            String description = section.getString("description", "No description.");
            Material material = Material.valueOf(section.getString("material", "BARRIER"));
            Location location = section.getLocation("location", new Location(Bukkit.getWorlds().get(0), 0, 64, 0));

            warps.add(new Warp(
                    name,
                    description,
                    material,
                    location
            ));

        });
        return warps;
    }

}
