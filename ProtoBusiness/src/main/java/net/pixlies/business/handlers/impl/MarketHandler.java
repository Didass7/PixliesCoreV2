package net.pixlies.business.handlers.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.Getter;
import net.pixlies.business.handlers.Handler;
import net.pixlies.business.market.challenge.Challenge;
import net.pixlies.business.market.profiles.OrderProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MarketHandler implements Handler {
    private final Map<UUID, OrderProfile> profiles = new HashMap<>();
    private final ListMultimap<String, Challenge> challenges = ArrayListMultimap.create();
}
