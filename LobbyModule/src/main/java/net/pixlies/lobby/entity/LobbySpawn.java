package net.pixlies.lobby.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.core.utils.location.LazyLocation;
import net.pixlies.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@ToString
public class LobbySpawn extends LazyLocation {

    private static final Lobby instance = Lobby.getInstance();

    private static final ModuleConfig warpsConfig = instance.getWarpsConfig();

    public LobbySpawn(Location location) {
        super(location);
    }

    public void save() {
        warpsConfig.set("spawn", this.getAsBukkitLocation());
        warpsConfig.save();
    }

    public static @NotNull LobbySpawn getSpawn() {
        Location spawnLoc = warpsConfig.getLocation("spawn");

        return new LobbySpawn(Objects.requireNonNullElseGet(spawnLoc, () ->
                Bukkit.getWorlds().get(0).getSpawnLocation()));

    }

}
