package net.pixlies.business.handlers;

import com.google.common.collect.ImmutableList;
import net.pixlies.business.ProtoBusiness;


public class RegisterHandlerManager {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    private final ImmutableList<Class<? extends Handler>> handlers = ImmutableList.of(
            // Handlers
    );

    public void registerAllHandlers() {
        handlers.forEach(clazz -> instance.getHandlerManager().register(clazz));
    }


}
