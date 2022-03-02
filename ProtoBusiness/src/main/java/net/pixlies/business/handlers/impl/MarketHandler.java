package net.pixlies.business.handlers.impl;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.handlers.Handler;

public class MarketHandler implements Handler {

    @Getter @Setter private volatile boolean marketOpen = true;

}
