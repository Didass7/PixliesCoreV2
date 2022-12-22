package net.pixlies.business.handlers.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.pixlies.business.handlers.Handler;
import net.pixlies.business.market.Challenge;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderProfile;

import java.util.HashMap;
import java.util.Map;

public class MarketHandler implements Handler {
    private final Map<String, OrderProfile> profiles = new HashMap<>();
    private final ListMultimap<String, Order> notifs = ArrayListMultimap.create();
    private final ListMultimap<String, Challenge> challenges = ArrayListMultimap.create();
    
    public Map<String, OrderProfile> getProfiles() {
        return profiles;
    }
    
    public ListMultimap<String, Order> getNotifs() {
        return notifs;
    }
    
    public ListMultimap<String, Challenge> getChallenges() {
        return challenges;
    }
}
