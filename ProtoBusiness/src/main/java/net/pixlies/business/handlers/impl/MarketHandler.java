package net.pixlies.business.handlers.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.Getter;
import net.pixlies.business.handlers.Handler;
import net.pixlies.business.market.challenges.Challenge;

@Getter
public class MarketHandler implements Handler {
    private final ListMultimap<String, Challenge> challenges = ArrayListMultimap.create();
}
