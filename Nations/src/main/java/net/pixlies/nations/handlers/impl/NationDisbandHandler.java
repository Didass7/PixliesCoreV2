package net.pixlies.nations.handlers.impl;

import lombok.Getter;
import net.pixlies.nations.handlers.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NationDisbandHandler implements Handler {

    /**
     * Represents all the confirmations for nation disbands.
     * The UUID key represents the player.
     * The String value represents the nation's id.
     */
    @Getter final Map<UUID, String> confirmations = new HashMap<>();

}
