package net.pixlies.core.handlers.impl;

import net.pixlies.core.handlers.Handler;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportHandler implements Handler {

    private final Map<UUID, Location> backLocations = new HashMap<>();
    private final Map<UUID, UUID> tpAsk = new HashMap<>();

    public Location getBackLocation(UUID uuid) {
        if (backLocations.get(uuid) == null) {
            return null;
        }
        return backLocations.get(uuid);
    }

    public void setBackLocation(UUID uuid, Location target) {
        backLocations.remove(uuid);
        backLocations.put(uuid, target);
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
        tpAsk.remove(target);
    }

}
