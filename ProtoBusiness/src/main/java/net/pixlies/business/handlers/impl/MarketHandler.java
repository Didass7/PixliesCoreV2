package net.pixlies.business.handlers.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.Getter;
import net.pixlies.business.handlers.Handler;
import net.pixlies.business.market.Challenge;

@Getter
public class MarketHandler implements Handler {
    // TODO: Move to MarketProfile
    private final ListMultimap<String, Challenge> challenges = ArrayListMultimap.create();
}
