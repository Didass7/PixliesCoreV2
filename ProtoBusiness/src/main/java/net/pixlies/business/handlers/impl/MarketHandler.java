package net.pixlies.business.handlers.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.Getter;
import net.pixlies.business.handlers.Handler;
import net.pixlies.business.market.Challenge;
import net.pixlies.business.market.orders.OrderProfile;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MarketHandler implements Handler {
    private final Map<String, OrderProfile> profiles = new HashMap<>();
    private final ListMultimap<String, Challenge> challenges = ArrayListMultimap.create();
}
