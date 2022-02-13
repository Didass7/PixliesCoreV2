package net.pixlies.proxy.handlers;

import com.google.common.collect.ImmutableList;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.handlers.impl.MaintenanceHandler;
import net.pixlies.proxy.handlers.impl.QueueHandler;

/**
 * @author Dynmie
 */
public class RegisterHandlerManager {

    private static final Proxy instance = Proxy.getInstance();

    private final ImmutableList<Class<? extends Handler>> handlers = ImmutableList.of(
            MaintenanceHandler.class,
            QueueHandler.class
    );

    public void registerAllHandlers() {
        handlers.forEach(clazz -> instance.getHandlerManager().register(clazz));
    }

}
