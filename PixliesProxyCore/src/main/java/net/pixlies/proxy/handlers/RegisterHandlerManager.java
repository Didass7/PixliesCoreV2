package net.pixlies.proxy.handlers;

import com.google.common.collect.ImmutableList;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.handlers.impl.AutoAnnounceHandler;

/**
 * @author Dynmie
 */
public class RegisterHandlerManager {

    private static final PixliesProxy instance = PixliesProxy.getInstance();

    private final ImmutableList<Class<? extends Handler>> handlers = ImmutableList.of(
            AutoAnnounceHandler.class
    );

    public void registerAll() {
        handlers.forEach(clazz -> instance.getHandlerManager().register(clazz));
    }

}
