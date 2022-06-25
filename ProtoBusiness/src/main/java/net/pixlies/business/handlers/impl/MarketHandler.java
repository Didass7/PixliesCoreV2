package net.pixlies.business.handlers.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.handlers.Handler;
import net.pixlies.business.market.Challenge;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketHandler implements Handler {

    @Getter @Setter private volatile boolean marketOpen = true;

    private Map<String, OrderProfile> profiles;
    private ListMultimap<String, Order> notifs;
    private ListMultimap<String, Challenge> challenges;

    public Map<String, OrderProfile> getProfiles() {
        if (profiles == null) profiles = new HashMap<>();
        return profiles;
    }

    public ListMultimap<String, Order> getNotifs() {
        if (notifs == null) notifs = ArrayListMultimap.create();
        return notifs;
    }

    public ListMultimap<String, Challenge> getChallenges() {
        if (challenges == null) challenges = ArrayListMultimap.create();
        return challenges;
    }

}
