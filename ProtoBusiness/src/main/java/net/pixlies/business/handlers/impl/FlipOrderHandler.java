package net.pixlies.business.handlers.impl;

import lombok.Getter;
import net.pixlies.business.handlers.Handler;
import net.pixlies.business.market.orders.Order;

import java.util.Map;
import java.util.UUID;

public class FlipOrderHandler implements Handler {

    @Getter private Map<UUID, Order[]> pendingFlips;

    public void addFlip(UUID uuid, Order previous, Order flip) {
        pendingFlips.put(uuid, new Order[]{ previous, flip });
    }

    public void removeFlip(UUID uuid) {
        pendingFlips.remove(uuid);
    }

}
