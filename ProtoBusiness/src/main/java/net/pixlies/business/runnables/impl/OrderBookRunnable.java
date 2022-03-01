package net.pixlies.business.runnables.impl;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.core.runnables.PixliesRunnable;

public class OrderBookRunnable extends PixliesRunnable {

    private final ProtoBusiness instance = ProtoBusiness.getInstance();

    public OrderBookRunnable() {
        super(true, 1L, 1L);
    }

    @Override
    public void run() {
        // TODO
    }

}
