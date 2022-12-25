package net.pixlies.core.handlers.impl;

import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.location.LazyLocation;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportHandler implements Handler {

    private final Map<UUID, LazyLocation> backLocations = new HashMap<>();

    public Location getBackLocation(UUID uuid) {
        if (backLocations.isEmpty()) return null;
        return backLocations.get(uuid).getAsBukkitLocation();
    }

    public void setBackLocation(UUID uuid, Location loc) {
        backLocations.remove(uuid);
        backLocations.put(uuid, new LazyLocation(loc));
    }

    public void removeBackLocation(UUID uuid) {
        if (backLocations.isEmpty()) return;
        backLocations.remove(uuid);
    }

}
