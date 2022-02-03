package net.pixlies.core.handlers.impl;

import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.location.LazyLocation;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportHandler implements Handler {

    private final Map<UUID, LazyLocation> backLocations = new HashMap<>();
    private final Map<UUID, UUID> tpAsk = new HashMap<>();

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

    public UUID getTpAskPlayer(UUID target) {
        if (tpAsk.get(target) == null) {
            return null;
        }
        return tpAsk.get(target);
    }

    public void setTpAskPlayer(UUID target, UUID sender) {
        removeTpAskPlayer(target);
        tpAsk.put(target, sender);
    }

    public void removeTpAskPlayer(UUID target) {
        if (backLocations.isEmpty()) return;
        tpAsk.remove(target);
    }

}
