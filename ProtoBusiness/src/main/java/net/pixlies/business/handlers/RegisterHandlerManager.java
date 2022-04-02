package net.pixlies.business.handlers;

import com.google.common.collect.ImmutableList;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.FlipOrderHandler;
import net.pixlies.business.handlers.impl.MarketHandler;


public class RegisterHandlerManager {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    private final ImmutableList<Class<? extends Handler>> handlers = ImmutableList.of(
            MarketHandler.class,
            FlipOrderHandler.class
    );

    public void registerAllHandlers() {
        handlers.forEach(clazz -> instance.getHandlerManager().register(clazz));
    }


}
