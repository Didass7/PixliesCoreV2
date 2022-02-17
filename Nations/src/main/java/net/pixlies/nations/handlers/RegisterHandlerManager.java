package net.pixlies.nations.handlers;

import com.google.common.collect.ImmutableList;
import net.pixlies.nations.Nations;
import net.pixlies.nations.handlers.impl.NationDisbandHandler;

public class RegisterHandlerManager {

    private static final Nations instance = Nations.getInstance();

    private final ImmutableList<Class<? extends Handler>> handlers = ImmutableList.of(
            NationDisbandHandler.class
    );

    public void registerAllHandlers() {
        handlers.forEach(clazz -> instance.getHandlerManager().register(clazz));
    }

}
